package com.swe681.scrabble.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;


@Getter //LOMBOK
@Setter //LOMBOK
@NoArgsConstructor //LOMBOK
@Entity //is a JPA annotation which specifies the class as an entity (so the class name can be used in JPQL queries)
@Table(name = "user") //annotation with the name attribute specifies the table name in the underlying database
public class User {
    @Id //declares the identifier property of the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @Transient
    private String passwordConfirm;

    @ManyToMany //defines a many-to-many relationship between 2 entities
    private Set<Role> roles;
}
