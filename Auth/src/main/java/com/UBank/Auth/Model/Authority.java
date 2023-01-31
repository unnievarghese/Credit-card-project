package com.UBank.Auth.Model;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@Table(name="authority")
public class Authority {

    private static final long serialVersionUID = 5769793636971877717L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false,length = 20)
    private String name;

    @ManyToMany(mappedBy = "authorities")
    private Collection<Role> roles;

    public Authority(){}

    public Authority(String name) {
        this.name = name;
    }

}
