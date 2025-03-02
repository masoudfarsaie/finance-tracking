package com.finst.assignment.financetracking.controller.mapper;

import com.finst.assignment.financetracking.controller.model.*;
import com.finst.assignment.financetracking.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {
    TransactionCommand toCommand(TransactionRequest request);

    AmendmentCommand toCommand(AmendmentRequest request);

    TransactionResponse toResponse(TransactionResult result);

    SearchResponse toResponse(TransactionRecords records);

    @Mapping(target = "date", source = "createdDate")
    TransactionEntry toTransactionEntry(TransactionRecord transactionRecord);

    default TransactionQuery toQuery(String iban, String category, LocalDateTime fromDate, LocalDateTime toDate,
                                     String sortBy, boolean descending, int page, int pageSize) {
        var pagination = PageRequest.of(page, pageSize);
        var direction = descending ? Sort.Direction.DESC : Sort.Direction.ASC;

        if ("date".equalsIgnoreCase(sortBy)) {
            pagination = pagination.withSort(Sort.by(direction, "createdDate"));
        } else if ("category".equalsIgnoreCase(sortBy)) {
            pagination = pagination.withSort(Sort.by(direction, "category"));
        }

        return new TransactionQuery(iban, category, fromDate, toDate, pagination);
    }
}
