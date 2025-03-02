package com.finst.assignment.financetracking.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionCommand (
        UUID requestId,
        String iban,
        String category,
        String description,
        BigDecimal amount
) {}
