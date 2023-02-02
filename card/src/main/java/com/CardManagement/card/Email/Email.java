package com.CardManagement.card.Email;

import com.CardManagement.card.Model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

@Service
public class Email {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendPdfAsEmailAttachment(String to, ByteArrayOutputStream out, Card card) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(to);
        helper.setSubject("Credit card bill for time period "+card.getBillingDate().minusMonths(1)+" to "+card.getBillingDate()+".");
        helper.setText("Please find the below your latest credit card purchase details.");
        InputStreamSource pdfSource = new ByteArrayResource(out.toByteArray());
        helper.addAttachment("Bill.pdf", pdfSource, "application/pdf");
        javaMailSender.send(mimeMessage);
    }

    public void notifyTransaction(Card card, Float amount, String toEmail) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(toEmail);
        helper.setSubject("Transaction notification.");
        helper.setText("Please find the below your latest credit card transaction details.\n Amount:" +amount+"\nDate: "+ LocalDate.now()+"\nLimit: "+card.getCreditLimit()+"\nBalance: "+card.getBalance());
        javaMailSender.send(mimeMessage);
    }
}
