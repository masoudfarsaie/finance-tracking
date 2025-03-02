package com.finst.assignment.financetracking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finst.assignment.financetracking.TestFixtures;
import com.finst.assignment.financetracking.controller.model.*;
import com.finst.assignment.financetracking.repository.AccountRepository;
import com.finst.assignment.financetracking.repository.TransactionRepository;
import com.finst.assignment.financetracking.repository.entity.TransactionEntity;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static com.finst.assignment.financetracking.TestFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class TransactionControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void init() {
        transactionRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void submit_withCorrectData_returnsOk() throws Exception {
        accountRepository.save(toAccount());
        mockMvc.perform(post(TestFixtures.URL_TRANSACTION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(new TransactionRequest(UUID.randomUUID(), "iban", "cat1", null, new BigDecimal("10.0")))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").exists())
                .andExpect(jsonPath("$.requestId").exists())
                .andExpect(jsonPath("$.newBalance").value("20.0"));

        var list = transactionRepository.findAll();

        assertThat(list).hasSize(1);
        assertThat(list.getFirst().getCategory()).isEqualTo("cat1");
        assertThat(list.getFirst().getAccount()).isNotNull();
        assertThat(list.getFirst().getAccount().getIban()).isEqualTo("iban");
        assertThat(list.getFirst().getAmount()).isEqualTo(new BigDecimal("10.00"));
        assertThat(list.getFirst().getNewBalance()).isEqualTo(new BigDecimal("20.00"));
        assertThat(list.getFirst().getCreatedDate()).isNotNull();
        assertThat(list.getFirst().getModifiedDate()).isNotNull();
    }

    @Test
    void submit_withNegativeAmount_returnsError() throws Exception {
        accountRepository.save(toAccount());
        mockMvc.perform(post(TestFixtures.URL_TRANSACTION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(new TransactionRequest(UUID.randomUUID(), "iban", "cat1", null, new BigDecimal("-30.0")))))
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    void submit_withNoIban_returnsError() throws Exception {
        mockMvc.perform(post(TestFixtures.URL_TRANSACTION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(new TransactionRequest(UUID.randomUUID(), "iban", "cat1", null, new BigDecimal("10.0")))))
                .andExpect(status().isUnprocessableEntity());

    }

    @Test
    void search_withCorrectData_returnsOk() throws Exception {
        accountRepository.save(toAccount());
        for (int i = 0; i < 10; i++) {
            createTransaction(mockMvc, i % 2 == 0 ? "c1" : "c2", "90.0");
            Thread.sleep(10);
        }

        var result = mockMvc.perform(get(TestFixtures.URL_TRANSACTION)
                        .param("iban", "iban")
                        .param("category", "c1"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var response = objectMapper.readValue(result, SearchResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getTotalPages()).isEqualTo(1);
        assertThat(response.getTotalElements()).isEqualTo(5);
        assertThat(response.getEntries())
                .hasSize(5)
                .extracting(TransactionEntry::getCategory, TransactionEntry::getAmount, TransactionEntry::getNewBalance)
                .containsExactly(
                        tuple("c1", new BigDecimal("90.00"), new BigDecimal("820.00")),
                        tuple("c1", new BigDecimal("90.00"), new BigDecimal("640.00")),
                        tuple("c1", new BigDecimal("90.00"), new BigDecimal("460.00")),
                        tuple("c1", new BigDecimal("90.00"), new BigDecimal("280.00")),
                        tuple("c1", new BigDecimal("90.00"), new BigDecimal("100.00"))
                );
    }

    @Test
    void search_withNoIban_returnsError() throws Exception {
        accountRepository.save(toAccount());
        mockMvc.perform(get(TestFixtures.URL_TRANSACTION)
                        .param("category", "c1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_withSingleData_returnsOk() throws Exception {
        accountRepository.save(toAccount());
        Integer txId = JsonPath.read(createTransaction(mockMvc, "c1", "90.0")
                .getResponse().getContentAsString(), "$.transactionId");

        mockMvc.perform(patch(TestFixtures.URL_TRANSACTION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(new AmendmentRequest(Long.valueOf(txId), null, null, new BigDecimal("10.0")))))
                .andExpect(status().isNoContent());

        var list = transactionRepository.findAll();

        assertThat(list).hasSize(1);
        assertThat(list.getFirst().getCategory()).isEqualTo("c1");
        assertThat(list.getFirst().getAccount()).isNotNull();
        assertThat(list.getFirst().getAccount().getIban()).isEqualTo("iban");
        assertThat(list.getFirst().getAccount().getBalance()).isEqualTo(new BigDecimal("20.00"));
        assertThat(list.getFirst().getAmount()).isEqualTo(new BigDecimal("10.00"));
        assertThat(list.getFirst().getNewBalance()).isEqualTo(new BigDecimal("20.00"));
        assertThat(list.getFirst().getCreatedDate()).isNotNull();
        assertThat(list.getFirst().getModifiedDate()).isNotNull();
    }

    @Test
    void update_withUpdatingMetadata_returnsOk() throws Exception {
        accountRepository.save(toAccount());
        Integer txId = JsonPath.read(createTransaction(mockMvc, "c1", "90.0")
                .getResponse().getContentAsString(), "$.transactionId");

        mockMvc.perform(patch(TestFixtures.URL_TRANSACTION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(new AmendmentRequest(Long.valueOf(txId), "c2", "desc", null))))
                .andExpect(status().isNoContent());

        var list = transactionRepository.findAll();

        assertThat(list).hasSize(1);
        assertThat(list.getFirst().getCategory()).isEqualTo("c2");
        assertThat(list.getFirst().getDescription()).isEqualTo("desc");
        assertThat(list.getFirst().getAccount()).isNotNull();
        assertThat(list.getFirst().getAccount().getIban()).isEqualTo("iban");
        assertThat(list.getFirst().getAccount().getBalance()).isEqualTo(new BigDecimal("100.00"));
        assertThat(list.getFirst().getAmount()).isEqualTo(new BigDecimal("90.00"));
        assertThat(list.getFirst().getNewBalance()).isEqualTo(new BigDecimal("100.00"));
        assertThat(list.getFirst().getCreatedDate()).isNotNull();
        assertThat(list.getFirst().getModifiedDate()).isNotNull();
    }

    @Test
    void update_withPostTransactions_returnsOk() throws Exception {
        accountRepository.save(toAccount());
        Integer txId = JsonPath.read(createTransaction(mockMvc, "c1", "90.0")
                .getResponse().getContentAsString(), "$.transactionId");

        for (int i = 0; i < 4; i++) {
            Thread.sleep(10);
            createTransaction(mockMvc, "c1", "90.0");
        }

        mockMvc.perform(patch(TestFixtures.URL_TRANSACTION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(new AmendmentRequest(Long.valueOf(txId), null, null, new BigDecimal("100.00")))))
                .andExpect(status().isNoContent());

        var list = transactionRepository.findAll();

        assertThat(list)
                .hasSize(5)
                .extracting(TransactionEntity::getAmount, TransactionEntity::getNewBalance)
                .containsExactlyInAnyOrder(
                        tuple(new BigDecimal("100.00"), new BigDecimal("110.00")),
                        tuple(new BigDecimal("90.00"), new BigDecimal("200.00")),
                        tuple(new BigDecimal("90.00"), new BigDecimal("290.00")),
                        tuple(new BigDecimal("90.00"), new BigDecimal("380.00")),
                        tuple(new BigDecimal("90.00"), new BigDecimal("470.00"))
                );

        assertThat(list.get(0).getAccount().getBalance()).isEqualTo(new BigDecimal("470.00"));
    }

    @Test
    void update_withPostTransactionsAndInvalidAmount_returnsError() throws Exception {
        accountRepository.save(toAccount());
        Integer txId = JsonPath.read(createTransaction(mockMvc, "c1", "90.0")
                .getResponse().getContentAsString(), "$.transactionId");

        for (int i = 0; i < 4; i++) {
            Thread.sleep(10);
            createTransaction(mockMvc, "c1", "-10.0");
        }

        mockMvc.perform(patch(TestFixtures.URL_TRANSACTION)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(new AmendmentRequest(Long.valueOf(txId), null, null, new BigDecimal("20.00")))))
                .andExpect(status().isUnprocessableEntity());

        var list = transactionRepository.findAll();

        assertThat(list)
                .hasSize(5)
                .extracting(TransactionEntity::getAmount, TransactionEntity::getNewBalance)
                .containsExactlyInAnyOrder(
                        tuple(new BigDecimal("90.00"), new BigDecimal("100.00")),
                        tuple(new BigDecimal("-10.00"), new BigDecimal("90.00")),
                        tuple(new BigDecimal("-10.00"), new BigDecimal("80.00")),
                        tuple(new BigDecimal("-10.00"), new BigDecimal("70.00")),
                        tuple(new BigDecimal("-10.00"), new BigDecimal("60.00"))
                );

        assertThat(list.get(0).getAccount().getBalance()).isEqualTo(new BigDecimal("60.00"));
    }

}