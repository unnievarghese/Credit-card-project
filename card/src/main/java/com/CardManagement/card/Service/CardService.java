package com.CardManagement.card.Service;

import com.CardManagement.card.CustomPinAuthentication.CustomPinAuthentication;
import com.CardManagement.card.Email.Email;
import com.CardManagement.card.Enum.CardDetails.*;
import com.CardManagement.card.FeignClient.AuthClient;
import com.CardManagement.card.FeignClient.BankClient;
import com.CardManagement.card.Io.AuthDto.ClientResponse;
import com.CardManagement.card.Io.Request.CardRequest;
import com.CardManagement.card.Io.Request.PurchaseRequest;
import com.CardManagement.card.Io.AuthDto.TransactionRequest;
import com.CardManagement.card.Io.Request.UpdateRequest;
import com.CardManagement.card.Io.Response.CardResponse;
import com.CardManagement.card.Model.Activity;
import com.CardManagement.card.Model.Card;
import com.CardManagement.card.Repository.ActivityRepository;
import com.CardManagement.card.Repository.CardRepository;
import com.CardManagement.card.Utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Component
public class CardService{

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthClient authClient;

    @Autowired
    private BankClient bankClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private Utils utils;

    @Autowired
    private CustomPinAuthentication customPinAuthentication;

    @Autowired
    private Email email;

    public CardResponse activateCard(CardRequest cardRequest, HttpServletRequest req){
        ClientResponse clientData = getClientDetails(req);
        Card card = save(createCardObject(clientData,cardRequest));
        return convertToResponse(card);
    }

    public CardResponse updateCardDetails(UpdateRequest updateRequest, HttpServletRequest req, String cardNumber, Integer clientId) throws IllegalAccessException, NoSuchFieldException {
        ClientResponse clientData = getClientById(req, clientId);
        Card card = cardRepository.findByCrnAndCardNumber(clientData.getCrn(), cardNumber);
        if (card != null) {
            Field[] fields = updateRequest.getClass().getDeclaredFields();
            for(Field field : fields){
                field.setAccessible(true);
                Object value = field.get(updateRequest);
                Field targetField = card.getClass().getDeclaredField(field.getName());
                targetField.setAccessible(true);
                if(value != null)
                    targetField.set(card, value);
            };

            card = updateDate(card);
        }
        return convertToResponse(save(card));
    }

    public String deactivateCard( String cardNumber, Integer clientId, HttpServletRequest req){
        ClientResponse clientData = getClientById(req, clientId);
        Card card = cardRepository.findByCrnAndCardNumber(clientData.getCrn(), cardNumber);
        if(card != null) {
            card.setIsDeactivated(true);
            save(card);
            return "Deactivation successful";
        }
        return "Deactivation unsuccessful";
    }

    public CardResponse makePurchase(PurchaseRequest purchaseRequest, HttpServletRequest req) throws Exception {
        ClientResponse clientData = getClientDetails(req);
        Card card = cardRepository.findByCrnAndCardNumberAndCvvAndServiceCode(clientData.getCrn(), purchaseRequest.getCardNumber(),purchaseRequest.getCvv(),purchaseRequest.getServiceCode());
        if(!checkCardStatus(card))
            throw new Exception("Card deactivated!");
        if(!customPinAuthentication.authenticatePin(purchaseRequest, card))
            throw new Exception("pin does not match!");
        validatePayment(purchaseRequest, card);
        createActivity(purchaseRequest.getAmount(), card, PaymentType.CREDIT);
        card = updateCard(purchaseRequest, card);
        card = updateDate(card);
        emailDriver(req, card, purchaseRequest.getAmount());
        return convertToResponse(card);
    }

    public CardResponse getPayment(TransactionRequest transactionRequest, HttpServletRequest req, String cardNumber){
        Card card = cardRepository.findByCardNumber(cardNumber);
        LinkedHashMap response = bankClient.handleCreditCardPayment(transactionRequest, req.getHeader("Authorization"));
        LinkedHashMap responseData = (LinkedHashMap) response.get("responseData");
        Boolean status =  (Boolean) responseData.get("data");
        if(status){
            Float updatedLimit = card.getCreditLimit() + transactionRequest.getAmount();
            Float updatedBalance = card.getBalance() - transactionRequest.getAmount();
            card.setCreditLimit(updatedLimit);
            card.setBalance(updatedBalance);
            createActivity(transactionRequest.getAmount(), card, PaymentType.DEBIT);
            emailDriver(req, card, transactionRequest.getAmount());
            card = updateDate(card);
            return convertToResponse(save(card));
        }
        return new CardResponse();
    }

    public Boolean receivePayment(TransactionRequest transactionRequest, HttpServletRequest req, String cardNumber){
        ClientResponse clientData = getClientDetails(req);
        Card card = cardRepository.findByCrnAndCardNumber(clientData.getCrn(), cardNumber);
        Float updatedLimit = card.getCreditLimit() + transactionRequest.getAmount();
        Float updatedBalance = card.getBalance() - transactionRequest.getAmount();
        card.setCreditLimit(updatedLimit);
        card.setBalance(updatedBalance);
        card = updateDate(card);
        save(card);
        emailDriver(req, card, transactionRequest.getAmount());
        createActivity(transactionRequest.getAmount(), card, PaymentType.CREDIT);
        return true;
    }

    @Scheduled(cron = "0 0 0 25 * ?")
    public List<byte[]> generateBill(){
        List<Card> cardList = cardRepository.findAll();
        return generatePDF(cardList);
    }

    public Card save(Card card) {
        return cardRepository.save(card);
    }

    public CardResponse convertToResponse(Card card) {
        CardResponse cardResponse = new CardResponse();
        modelMapper.map(card, cardResponse);
        return cardResponse;
    }

    public Card createCardObject(ClientResponse clientData, CardRequest cardRequest){
        Card card = new Card();
        card.setCardNumber(utils.generateNumber(16));
        card.setCrn(clientData.getCrn());
        card.setExpiry(utils.getExpiry());
        card.setCreatedAt(LocalDate.now());
        card.setUpdatedAt(LocalDate.now());
        card.setCvv(utils.generateNumber(3));
        card.setEncryptedPin(bCryptPasswordEncoder.encode(cardRequest.getPin()));
        card.setServiceCode(utils.generateRandomString(9));
        card.setBillingDate(utils.billingDate());
        card.setGracePeriod(utils.gracePeriod());
        card.setApr(utils.getApr());
        card.setIsDue(false);
        card.setMinimumPaymentPercent(utils.getMpp());
        card.setCreditLimit(utils.getCreditLimit());
        card.setBalance(0F);
        card.setIsDeactivated(false);
        return card;
    }

    public void validatePayment(PurchaseRequest purchaseRequest, Card card) throws Exception {
        if(!purchaseRequest.getExpiry().isAfter(LocalDate.now()))
            throw new Exception("Card Expired!");
        if(card.getIsDue())
            throw new Exception("Transaction declined due to outstanding balance!");
        if(purchaseRequest.getAmount() > card.getCreditLimit())
            throw new Exception("Amount exceeds limit!");
    }

    public Card updateCard(PurchaseRequest purchaseRequest, Card card){
        Float updatedLimit = card.getCreditLimit() - purchaseRequest.getAmount();
        Float updatedBalance = card.getBalance() + purchaseRequest.getAmount();
        card.setCreditLimit(updatedLimit);
        card.setBalance(updatedBalance);
        return save(card);
    }

    public ClientResponse getClientDetails(HttpServletRequest req){
        LinkedHashMap response = authClient.getClientDetails(req.getHeader("Authorization"));
        LinkedHashMap responseData = (LinkedHashMap) response.get("responseData");
        return objectMapper.convertValue((LinkedHashMap) responseData.get("data"), ClientResponse.class);
    }

    public ClientResponse getClientById(HttpServletRequest req, Integer clientId){
        LinkedHashMap response = authClient.getClientById(req.getHeader("Authorization"), clientId);
        LinkedHashMap responseData = (LinkedHashMap) response.get("responseData");
        return objectMapper.convertValue((LinkedHashMap) responseData.get("data"), ClientResponse.class);
    }

    public String getClientEmail(HttpServletRequest req, String crn){
        LinkedHashMap response = authClient.getClientDetailsByCrn(req.getHeader("Authorization"), crn);
        LinkedHashMap responseData = (LinkedHashMap) response.get("responseData");
        return objectMapper.convertValue((LinkedHashMap) responseData.get("data"), ClientResponse.class).getEmail();
    }

    public void createActivity(Float amount, Card card, PaymentType paymentType){
        Activity activity = new Activity();
        activity.setCardDetails(card);
        activity.setDate(LocalDate.now());
        activity.setAmount(amount);
        activity.setPaymentType(paymentType);
        activity.setReference(paymentType.toString()+"_"+card.getCrn()+"_"+LocalDate.now());
        activityRepository.save(activity);
    }

    public List<byte[]> generatePDF(List<Card> cardList){
        List<byte[]> outList = new ArrayList<>();
        for(Card card : cardList) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document();
            try {
                PdfWriter.getInstance(document, out);
                document.open();
                Font font = new Font();
                font.setSize(14);
                Paragraph paragraph = new Paragraph("Here is the list of all credit purchases from "+card.getBillingDate().minusMonths(1)+" to" +card.getBillingDate()+".", font);
                paragraph.setSpacingAfter(15f);
                document.add(paragraph);
                PdfPTable table = new PdfPTable(4);
                table.setWidthPercentage(100);
                table.addCell("Card Number");
                table.addCell("Credited Amount");
                table.addCell("Date");
                table.addCell("Reference");
                for(Activity activity : card.getActivities()){
                    table.addCell(activity.getCardDetails().getCardNumber());
                    table.addCell(activity.getAmount().toString());
                    table.addCell(activity.getDate().toString());
                    table.addCell(activity.getReference());
                }
                Paragraph lastLine = new Paragraph("The total balance to be paid on or before "+card.getBillingDate().plusDays(card.getGracePeriod())+" is "+ card.getBalance()+".");
                document.add(table);
                document.add(lastLine);
                document.close();
                LinkedHashMap response = authClient.getClientDetailsByCrn(utils.getToken(), card.getCrn());
                LinkedHashMap responseData = (LinkedHashMap) response.get("responseData");
                String toEmail = objectMapper.convertValue((LinkedHashMap) responseData.get("data"), ClientResponse.class).getEmail();
                CompletableFuture.runAsync(()->{
                    try {
                        email.sendPdfAsEmailAttachment(toEmail, out, card);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                });
                outList.add(out.toByteArray());
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
        }
        return outList;
    }

    public byte[] convertToZip(List<byte[]> out) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipArchiveOutputStream zaos = new ZipArchiveOutputStream(baos);
        int count = 0;
        for (byte[] data : out) {
            ZipArchiveEntry entry = new ZipArchiveEntry("file" + count + ".pdf");
            zaos.putArchiveEntry(entry);

            // Write contents of the file
            zaos.write(data);
            zaos.closeArchiveEntry();
            count ++;
        }
        zaos.finish();
        return baos.toByteArray();
    }

    public void emailDriver(HttpServletRequest req, Card card, Float amount){
        String toEmail = getClientEmail(req, card.getCrn());
        CompletableFuture.runAsync(()->{
            try {
                email.notifyTransaction(card, amount, toEmail);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public Boolean checkCardStatus(Card card){
        return !card.getIsDeactivated();
    }

    public Card updateDate(Card card){
         card.setUpdatedAt(LocalDate.now());
         return card;
    }
}
