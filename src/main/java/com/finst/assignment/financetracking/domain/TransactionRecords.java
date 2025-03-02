package com.finst.assignment.financetracking.domain;

import java.math.BigDecimal;
import java.util.List;

public record TransactionRecords (
    Integer totalPages,
    long totalElements,
    Integer size,
    Integer page,
    boolean empty,
    List<TransactionRecord> entries
) {}
