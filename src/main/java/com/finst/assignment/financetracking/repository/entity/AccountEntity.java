package com.finst.assignment.financetracking.repository.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_account", indexes = @Index(name = "uniqueIban", columnList = "iban", unique = true))
public class AccountEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String iban;

    private BigDecimal balance = BigDecimal.ZERO;

    private BigDecimal minBalance = BigDecimal.ZERO;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
