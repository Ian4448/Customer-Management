package com.borges.project.service;

import com.borges.project.dao.CustomerDAO;
import com.borges.project.dao.TokenRepository;
import com.borges.project.errorhandling.CustomerRequestException;
import com.borges.project.model.Customer;
import com.borges.project.model.LoginForm;
import com.borges.project.model.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class CustomerDatabaseService {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private CustomerDAO customerDAO;
    @Autowired
    private TokenRepository tokenRepository;

    public Customer addCustomer(Customer customer) {
        return customerDAO.save(customer);
    }

    public Customer updateCustomer(String userName, Customer customer) {
        customer.setUserName(userName);
        return customerDAO.save(customer);
    }

    public void deleteCustomer(String userName) {
        customerDAO.deleteById(userName);
    }

    public Customer getCustomerById(String userName) {

        Optional<Customer> optionalCustomer = customerDAO.findById(userName);

        if (optionalCustomer.isEmpty())
            throw new CustomerRequestException(String.format("Customer with name %s not found", userName));

        return optionalCustomer.get();
    }

    public List<Customer> getCustomers() {
        return customerDAO.findAll();
    }

    public Token storeTrustedToken(String userName, String tokenStr) {
        Customer customer = getCustomerById(userName);
        Token token = new Token(tokenStr, customer, LocalDateTime.now().plusDays(30));
        tokenRepository.save(token);
        return token;
    }

    public boolean isTrustedTokenValid(String tokenStr) {
        Optional<Token> token = tokenRepository.findByToken(tokenStr);
        return token.isPresent() && token.get().getExpirationDate().isAfter(LocalDateTime.now());
    }

    public boolean hasUserName(String userName) {
        return customerDAO
                .findById(userName)
                .isPresent();
    }

    // Todo(7/23/2024) make email secondary id in h2 database
    public boolean hasEmail(String email) {
        return customerDAO
                .findAll()
                .stream()
                .anyMatch(customer -> customer.getEmail().equals(email));
    }

    public boolean hasLogin(LoginForm loginForm) {
        return (customerDAO
                .findAll()
                .stream()
                .anyMatch(customer ->
                        customer.getUserName().equals(loginForm.getUsername())
                                && passwordEncoder.matches(loginForm.getPassword(), customer.getPassword())));
    }
}
