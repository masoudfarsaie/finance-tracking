package com.finst.assignment.financetracking.controller.model;

import com.finst.assignment.financetracking.repository.entity.AccountEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionEntry {
    private Long id;

    private UUID requestId;

    private String iban;

    private String category;

    private String description;

    private BigDecimal amount;

    private BigDecimal newBalance;

    private LocalDateTime date;
}
