package com.finst.assignment.financetracking.controller;

import com.finst.assignment.financetracking.controller.api.AccountApi;
import com.finst.assignment.financetracking.controller.mapper.AccountMapper;
import com.finst.assignment.financetracking.controller.model.CreateAccountRequest;
import com.finst.assignment.financetracking.controller.model.CreateAccountResponse;
import com.finst.assignment.financetracking.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class AccountController implements AccountApi {
    private final AccountService accountService;

    private final AccountMapper accountMapper;

    @Override
    public ResponseEntity<CreateAccountResponse> create(CreateAccountRequest request) {
        var command = accountMapper.toAccountCommand(request);
        return ResponseEntity.ok(accountMapper.toResponse(accountService.createAccount(command)));
    }
}
