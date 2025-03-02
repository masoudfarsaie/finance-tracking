package com.finst.assignment.financetracking.controller.mapper;

import com.finst.assignment.financetracking.controller.model.CreateAccountRequest;
import com.finst.assignment.financetracking.controller.model.CreateAccountResponse;
import com.finst.assignment.financetracking.domain.CreateAccountCommand;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {
    CreateAccountCommand toAccountCommand(CreateAccountRequest request);

    CreateAccountResponse toResponse(Long id);
}
