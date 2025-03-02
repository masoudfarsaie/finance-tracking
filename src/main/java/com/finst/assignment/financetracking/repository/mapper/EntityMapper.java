package com.finst.assignment.financetracking.repository.mapper;

import com.finst.assignment.financetracking.domain.CreateAccountCommand;
import com.finst.assignment.financetracking.domain.TransactionCommand;
import com.finst.assignment.financetracking.domain.TransactionRecord;
import com.finst.assignment.financetracking.domain.TransactionRecords;
import com.finst.assignment.financetracking.repository.entity.AccountEntity;
import com.finst.assignment.financetracking.repository.entity.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EntityMapper {
    @Mapping(target = "balance", constant = "0.00")
    AccountEntity toEntity(CreateAccountCommand command);

    TransactionEntity toTransaction(TransactionCommand command);

    @Mapping(target = "entries", source = "content")
    TransactionRecords toTransactionRecords(Page<TransactionEntity> entityPage);

    @Mapping(target = "iban", source = "account.iban")
    TransactionRecord toTransactionRecord(TransactionEntity entity);
}
