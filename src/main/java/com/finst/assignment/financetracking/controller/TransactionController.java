package com.finst.assignment.financetracking.controller;

import com.finst.assignment.financetracking.controller.api.TransactionApi;
import com.finst.assignment.financetracking.controller.mapper.TransactionMapper;
import com.finst.assignment.financetracking.controller.model.*;
import com.finst.assignment.financetracking.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@AllArgsConstructor
@RestController
public class TransactionController implements TransactionApi {
    private TransactionService transactionService;

    private final TransactionMapper mapper;

    @Override
    public ResponseEntity<TransactionResponse> submit(TransactionRequest request) {
        var command = mapper.toCommand(request);
        return ResponseEntity.ok(mapper.toResponse(transactionService.submit(command)));
    }

    @Override
    public ResponseEntity<Void> update(AmendmentRequest request) {
        transactionService.update(mapper.toCommand(request));

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<SearchResponse> find(
            String iban, String category, LocalDateTime fromDate, LocalDateTime toDate, String sortBy,
            boolean descending, int page, int pageSize) {
        var query = mapper.toQuery(iban, category, fromDate, toDate, sortBy, descending, page, pageSize);

        return ResponseEntity.ok(mapper.toResponse(transactionService.search(query)));
    }

}
