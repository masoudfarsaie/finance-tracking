package com.finst.assignment.financetracking.controller.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SearchResponse {
    private Integer totalPages;

    private long totalElements;

    private Integer size;

    private Integer page;

    private boolean empty;

    private List<TransactionEntry> entries;
}
