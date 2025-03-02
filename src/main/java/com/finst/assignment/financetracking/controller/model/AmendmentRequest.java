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
public class AmendmentRequest {
    @NotNull
    private Long transactionId;

    private String category;

    private String description;

    private BigDecimal amount;
}
