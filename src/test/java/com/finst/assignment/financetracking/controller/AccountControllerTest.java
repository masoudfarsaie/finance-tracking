package com.finst.assignment.financetracking.controller;

import com.finst.assignment.financetracking.TestFixtures;
import com.finst.assignment.financetracking.controller.model.CreateAccountRequest;
import com.finst.assignment.financetracking.repository.AccountRepository;
import com.finst.assignment.financetracking.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static com.finst.assignment.financetracking.TestFixtures.asJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void init() {
        accountRepository.deleteAll();
        transactionRepository.deleteAll();
    }

    @Test
    void createAccount_withCorrectData_returnsOk() throws Exception {
        mockMvc.perform(post(TestFixtures.URL_ACCOUNT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(new CreateAccountRequest("title", "iban", new BigDecimal("10.00")))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());

        var list = accountRepository.findAll();

        assertThat(list).hasSize(1);
        assertThat(list.getFirst().getBalance()).isEqualTo(new BigDecimal("0.00"));
        assertThat(list.getFirst().getTitle()).isEqualTo("title");
        assertThat(list.getFirst().getIban()).isEqualTo("iban");
        assertThat(list.getFirst().getMinBalance()).isEqualTo(new BigDecimal("10.00"));
        assertThat(list.getFirst().getCreatedDate()).isNotNull();
        assertThat(list.getFirst().getModifiedDate()).isNotNull();
    }

    @Test
    void createAccount_withInvalidData_returnsError() throws Exception {
        mockMvc.perform(post(TestFixtures.URL_ACCOUNT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(new CreateAccountRequest("title", null, null))))
                .andExpect(status().isBadRequest());
    }
}