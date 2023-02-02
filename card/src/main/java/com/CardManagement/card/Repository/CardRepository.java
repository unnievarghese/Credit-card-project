package com.CardManagement.card.Repository;

import com.CardManagement.card.Model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CardRepository extends JpaRepository<Card, Integer>{

    Card findByCrnAndCardNumberAndCvvAndServiceCode(String crn, String cardNumber, String cvv, String serviceCode);

    Card findByCrnAndCardNumber(String crn, String cardNumber);

    Card findByCardNumber(String cardNumber);

    @Query(value = "SELECT c.* FROM card_details c JOIN activity a ON c.card_id = a.card_id WHERE a.payment_type = 1", nativeQuery = true)
    List<Card> findByPaymentType();
}
