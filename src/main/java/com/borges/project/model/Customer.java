package com.borges.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Customer {
    @Id
    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Builder(toBuilder = true)
    public Customer(String firstName, String lastName, String email, String userName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userName = userName;
        this.password = password;
    }

    // Method to clear the first name field
    public Customer clearFirstName() {
        return this.toBuilder().firstName(null).build();
    }

    // Method to clear the last name field
    public Customer clearLastName() {
        return this.toBuilder().lastName(null).build();
    }

    // Method to clear the email field
    public Customer clearEmail() {
        return this.toBuilder().email(null).build();
    }

    // Method to clear the username field
    public Customer clearUserName() {
        return this.toBuilder().userName(null).build();
    }

    // Method to clear the password field
    public Customer clearPassword() {
        return this.toBuilder().password(null).build();
    }
}
