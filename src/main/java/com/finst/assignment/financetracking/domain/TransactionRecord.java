package com.finst.assignment.financetracking.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionRecord (
        Long id,
        UUID requestId,
        String iban,
        String category,
        String description,
        BigDecimal amount,
        BigDecimal newBalance,
        LocalDateTime createdDate
) { }
