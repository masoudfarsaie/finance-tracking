package com.finst.assignment.financetracking.domain;

import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionQuery (
        String iban,
        String category,
        LocalDateTime fromDate,
        LocalDateTime toDate,
        PageRequest pageRequest
) {
}
