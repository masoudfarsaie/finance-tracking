package com.finst.assignment.financetracking.service;

import com.finst.assignment.financetracking.domain.*;
import com.finst.assignment.financetracking.repository.AccountRepository;
import com.finst.assignment.financetracking.repository.TransactionRepository;
import com.finst.assignment.financetracking.repository.entity.TransactionEntity;
import com.finst.assignment.financetracking.repository.mapper.EntityMapper;
import com.finst.assignment.financetracking.repository.query.TxSpecifications;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
@Transactional
public class TransactionService {
    private final TransactionRepository txRepository;
    private final AccountRepository accountRepository;
    private final EntityMapper entityMapper;

    @Transactional(readOnly = true)
    public TransactionRecords search(TransactionQuery query) {
        var spec = Specification.where(TxSpecifications.hasIban(query.iban()));

        if (StringUtils.isNotBlank(query.category())) {
            spec = spec.and(TxSpecifications.hasCategory(query.category()));
        }

        if (query.toDate() != null) {
            spec = spec.and(TxSpecifications.toDate(query.toDate()));
        }

        if (query.fromDate() != null) {
            spec = spec.and(TxSpecifications.fromDate(query.fromDate()));
        }

        return entityMapper.toTransactionRecords(txRepository.findAll(spec, query.pageRequest()));
    }

    public TransactionResult submit(TransactionCommand command) {
        var account = accountRepository.findByIban(command.iban())
                .orElseThrow(() -> new NoSuchElementException("account not found"));

        var transaction = entityMapper.toTransaction(command);
        var newBalance = account.getBalance().add(command.amount());

        if (newBalance.compareTo(account.getMinBalance()) < 0) {
            throw new ArithmeticException("account is empty");
        }

        account.setBalance(newBalance);
        transaction.setNewBalance(newBalance);
        transaction.setAccount(account);
        transaction = txRepository.save(transaction);

        return new TransactionResult(transaction.getId(), command.requestId(), newBalance);
    }

    public void update(AmendmentCommand command) {
        var trans = txRepository.findById(command.transactionId())
                .orElseThrow(() -> new NoSuchElementException("transaction not found"));

        if (command.category() != null) {
            trans.setCategory(command.category());
        }

        if (command.description() != null) {
            trans.setDescription(command.description());
        }

        if (command.amount() != null) {
            updateAmount(trans, command.amount());
        }
    }

    private void updateAmount(TransactionEntity transaction, BigDecimal newAmount) {
        if (newAmount.equals(transaction.getAmount())) {
            return;
        }

        var diff = newAmount.subtract(transaction.getAmount());
        var account = accountRepository.findByIban(transaction.getAccount().getIban())
                .orElseThrow(() -> new NoSuchElementException("account not found"));

        transaction.setAmount(newAmount);
        account.setBalance(account.getBalance().add(diff));
        updateNewBalance(transaction, account.getMinBalance(), diff);

        txRepository.findByAccountAndCreatedDateAfter(account, transaction.getCreatedDate())
                .forEach(tx -> updateNewBalance(tx, account.getMinBalance(), diff));
    }

    private void updateNewBalance(TransactionEntity transaction, BigDecimal limit, BigDecimal diff) {
        transaction.setNewBalance(transaction.getNewBalance().add(diff));

        if (transaction.getNewBalance().compareTo(limit) < 0) {
            throw new ArithmeticException("account will be empty on transaction " + transaction.getId());
        }
    }
}
