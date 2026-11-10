package com.fint.service;

import com.fint.model.Account;
import com.fint.model.Transaction;
import com.fint.repository.AccountRepository;
import com.fint.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LedgerService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Transaction executeTransaction(String accountNumber, BigDecimal amount, String type, String category, String refCode) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be strictly positive.");
        }

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountNumber));

        if ("DEBIT".equalsIgnoreCase(type)) {
            if (account.getBalance().compareTo(amount) < 0) {
                throw new IllegalStateException("Insufficient funds for debit.");
            }
            account.setBalance(account.getBalance().subtract(amount));
        } else if ("CREDIT".equalsIgnoreCase(type)) {
            account.setBalance(account.getBalance().add(amount));
        } else {
            throw new IllegalArgumentException("Invalid transaction type: " + type);
        }

        accountRepository.save(account);

        Transaction tx = Transaction.builder()
                .account(account)
                .amount(amount)
                .type(type.toUpperCase())
                .category(category)
                .referenceCode(refCode)
                .timestamp(Instant.now())
                .build();

        return transactionRepository.save(tx);
    }

    public List<Transaction> getHistory(String accountNumber) {
        return transactionRepository.findByAccount_AccountNumberOrderByTimestampDesc(accountNumber);
    }
}
