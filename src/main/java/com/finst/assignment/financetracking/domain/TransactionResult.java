package com.finst.assignment.financetracking.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionResult(
        Long transactionId,
        UUID requestId,
        BigDecimal newBalance
) { }
