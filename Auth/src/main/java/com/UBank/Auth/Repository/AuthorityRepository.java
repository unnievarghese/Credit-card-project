package com.UBank.Auth.Repository;

import com.UBank.Auth.Model.Authority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends CrudRepository<Authority,Long> {
    Authority findByName(String name);
}
