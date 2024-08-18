package com.borges.project;

import com.borges.project.dao.CustomerDAO;
import com.borges.project.dao.TokenRepository;
import com.borges.project.model.Customer;
import com.borges.project.model.Token;
import com.borges.project.service.CustomerDatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class CustomerDatabaseServiceTest {

    @Mock
    private CustomerDAO customerDAO;

    @Mock
    private TokenRepository tokenRepository;

    @InjectMocks
    private CustomerDatabaseService customerDatabaseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIsTrustedTokenValid() {
        String tokenStr = "valid-token";
        Token token = new Token(tokenStr, new Customer(), LocalDateTime.now().plusDays(1));

        when(tokenRepository.findByToken(tokenStr)).thenReturn(Optional.of(token));

        boolean result = customerDatabaseService.isTrustedTokenValid(tokenStr);
        assertTrue(result);
    }
}
