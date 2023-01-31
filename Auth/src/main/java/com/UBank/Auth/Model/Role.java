package com.UBank.Auth.Model;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@Table(name="roles")
public class Role {

    private static final long serialVersionUID = 2329281802878721981L;
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false,length = 20)
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<Client> client;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name="role_authorities",
            joinColumns = @JoinColumn(name="role_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id",referencedColumnName = "id"))
    private Collection<Authority> authorities;

    public Role(){}

    public Role(String name) {
        this.name = name;
    }

}
