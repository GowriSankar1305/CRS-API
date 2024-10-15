package com.eazybyts.boot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eazybyts.boot.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
