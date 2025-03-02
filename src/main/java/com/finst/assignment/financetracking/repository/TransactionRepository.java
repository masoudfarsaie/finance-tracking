package com.finst.assignment.financetracking.repository;

import com.finst.assignment.financetracking.repository.entity.AccountEntity;
import com.finst.assignment.financetracking.repository.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long>,
        JpaSpecificationExecutor<TransactionEntity> {

    List<TransactionEntity> findByAccountAndCreatedDateAfter(AccountEntity account, LocalDateTime date);
}
