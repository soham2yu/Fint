package com.fint.repository;

import com.fint.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByAccount_AccountNumberOrderByTimestampDesc(String accountNumber);
}
