package com.BankingServices.UBank.Repository;

import com.BankingServices.UBank.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findByAccountNumber (String accountNumber);
}
