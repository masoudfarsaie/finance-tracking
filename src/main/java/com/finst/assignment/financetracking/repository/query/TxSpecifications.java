package com.finst.assignment.financetracking.repository.query;

import com.finst.assignment.financetracking.repository.entity.TransactionEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TxSpecifications {

    public static Specification<TransactionEntity> hasIban(String iban) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("account").get("iban"), iban);
    }

    public static Specification<TransactionEntity> hasCategory(String category) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("category"), "%" + category + "%");
    }

    public static Specification<TransactionEntity> toDate(LocalDateTime toDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), toDate);
    }

    public static Specification<TransactionEntity> fromDate(LocalDateTime fromDate) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), fromDate);
    }
}
