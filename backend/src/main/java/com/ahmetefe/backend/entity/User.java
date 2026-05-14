package com.ahmetefe.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String surname;

    @Column(length = 100)
    private String mail;

    @Column(length = 100)
    private String password;
}
