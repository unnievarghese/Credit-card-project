package com.UBank.Auth.Repository;

import com.UBank.Auth.Model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

@Repository
@Transactional
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Client findByEmail(String email);

    Client findByCrn(String crn);
}
