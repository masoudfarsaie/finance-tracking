package com.finst.assignment.financetracking.controller.api;

import com.finst.assignment.financetracking.controller.model.CreateAccountRequest;
import com.finst.assignment.financetracking.controller.model.CreateAccountResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Accounts Api")
@RequestMapping("/account")
public interface AccountApi {

    @PostMapping
    ResponseEntity<CreateAccountResponse> create(@Valid @RequestBody CreateAccountRequest request);

}
