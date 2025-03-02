package com.finst.assignment.financetracking.controller.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransactionResponse {
    private Long transactionId;

    private UUID requestId;

    private BigDecimal newBalance;
}
