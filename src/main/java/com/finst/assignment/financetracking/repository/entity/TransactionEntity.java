package com.finst.assignment.financetracking.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_transactions", indexes = @Index(name = "uniqueRequest", columnList = "requestId", unique = true))
public class TransactionEntity {
    @Id
    @GeneratedValue
    private Long id;

    private UUID requestId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    private String category;

    private String description;

    private BigDecimal amount;

    private BigDecimal newBalance;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
