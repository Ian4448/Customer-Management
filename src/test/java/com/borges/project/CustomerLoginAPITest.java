package com.borges.project;

import com.borges.project.api.CustomerLoginAPI;
import com.borges.project.service.CustomerDatabaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerLoginAPI.class)
public class CustomerLoginAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerDatabaseService customerDatabaseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginWithValidToken() throws Exception {
        String token = "valid-token";

        when(customerDatabaseService.isTrustedTokenValid(token)).thenReturn(true);

        mockMvc.perform(get("/login").param("token", token))
                .andExpect(status().is2xxSuccessful());
    }
}

