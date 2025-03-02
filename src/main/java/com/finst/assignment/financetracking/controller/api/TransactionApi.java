package com.finst.assignment.financetracking.controller.api;

import com.finst.assignment.financetracking.controller.model.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "Transaction Api")
@RequestMapping("/transaction")
public interface TransactionApi {

    @PostMapping
    ResponseEntity<TransactionResponse> submit(@Valid @RequestBody TransactionRequest request);

    @PatchMapping
    ResponseEntity<Void> update(@Valid @RequestBody AmendmentRequest request);

    @GetMapping
    ResponseEntity<SearchResponse> find(@RequestParam(name = "iban") String iban,
                                        @RequestParam(name = "category", required = false) String category,
                                        @RequestParam(name = "fromDate", required = false) LocalDateTime fromDate,
                                        @RequestParam(name = "toDate", required = false) LocalDateTime toDate,
                                        @RequestParam(name = "sortBy", required = false, defaultValue = "date") String sortBy,
                                        @RequestParam(name = "descending", required = false, defaultValue = "true") boolean descending,
                                        @RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                        @RequestParam(name = "pageSize", required = false, defaultValue = "50") int pageSize);
}
