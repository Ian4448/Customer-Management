package com.borges.project.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
public final class Token {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "userName", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    @Getter
    private LocalDateTime expirationDate;

    public Token() {
    }

    public Token(String token, Customer customer, LocalDateTime expirationDate) {
        this.token = token;
        this.customer = customer;
        this.expirationDate = expirationDate;
    }
}
