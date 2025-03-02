package com.finst.assignment.financetracking.domain;

import jakarta.validation.constraints.NotEmpty;

import java.math.BigDecimal;

public record CreateAccountCommand (
        String title,
        String iban,
        BigDecimal minBalance
){ }
