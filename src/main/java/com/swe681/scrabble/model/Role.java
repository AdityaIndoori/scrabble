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
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles") //mappedBy indicates the entity is the inverse of the relationship
    private Set<User> users;
}
