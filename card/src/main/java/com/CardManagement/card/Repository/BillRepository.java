package com.CardManagement.card.Repository;

import com.CardManagement.card.Model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

@Repository
@Transactional
public interface BillRepository extends JpaRepository<Bill, Integer>{
}
