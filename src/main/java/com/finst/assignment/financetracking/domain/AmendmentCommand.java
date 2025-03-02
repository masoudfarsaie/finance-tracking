package com.finst.assignment.financetracking.domain;

import java.math.BigDecimal;

public record AmendmentCommand(
        Long transactionId,
        String category,
        String description,
        BigDecimal amount
) { }
