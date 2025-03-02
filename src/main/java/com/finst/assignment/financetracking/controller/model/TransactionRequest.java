package com.finst.assignment.financetracking.controller.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    @NotNull
    private UUID requestId;
    @NotEmpty
    private String iban;

    private String category;

    private String description;
    @NotNull
    private BigDecimal amount;
}
