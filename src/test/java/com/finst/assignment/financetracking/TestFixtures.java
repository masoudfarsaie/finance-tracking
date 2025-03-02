package com.finst.assignment.financetracking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finst.assignment.financetracking.controller.model.TransactionRequest;
import com.finst.assignment.financetracking.repository.entity.AccountEntity;
import com.finst.assignment.financetracking.repository.entity.TransactionEntity;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestFixtures {
    public final static String URL_ACCOUNT = "/account";
    public final static String URL_TRANSACTION = "/transaction";

    public static String asJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static AccountEntity toAccount() {
        var account = new AccountEntity();
        account.setIban("iban");
        account.setTitle("title");
        account.setMinBalance(BigDecimal.ZERO);
        account.setBalance(new BigDecimal("10.00"));

        return account;
    }

    public static MvcResult createTransaction(MockMvc mockMvc, String cat, String amount) throws Exception {
        return mockMvc.perform(post(TestFixtures.URL_TRANSACTION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(new TransactionRequest(UUID.randomUUID(), "iban", cat, null, new BigDecimal(amount)))))
                .andExpect(status().isOk())
                .andReturn();
    }
}
