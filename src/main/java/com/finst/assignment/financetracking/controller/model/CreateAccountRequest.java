package com.finst.assignment.financetracking.controller.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {
    @NotEmpty
    private String title;
    @NotEmpty
    private String iban;
    @NotNull
    private BigDecimal minBalance;
}
