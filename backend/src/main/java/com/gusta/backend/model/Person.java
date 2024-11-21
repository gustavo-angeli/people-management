package com.gusta.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String email;
    private String password;
//    @ElementCollection
//    @CollectionTable(name = "person_roles") // Cria uma tabela para armazenar os enums
//    @Enumerated(EnumType.STRING)
//    @Column(name = "role")
//    private List<Role> roles;

}
