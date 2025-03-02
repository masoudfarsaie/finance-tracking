package com.finst.assignment.financetracking.service;

import com.finst.assignment.financetracking.domain.CreateAccountCommand;
import com.finst.assignment.financetracking.repository.AccountRepository;
import com.finst.assignment.financetracking.repository.mapper.EntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
@Transactional
public class AccountService {
    private final AccountRepository repository;
    private final EntityMapper entityMapper;

    public Long createAccount(CreateAccountCommand command) {
        return repository.save(entityMapper.toEntity(command)).getId();
    }
}
