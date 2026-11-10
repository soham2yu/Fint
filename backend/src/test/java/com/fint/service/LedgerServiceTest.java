package com.fint.service;

import com.fint.model.Account;
import com.fint.model.Transaction;
import com.fint.repository.AccountRepository;
import com.fint.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LedgerServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private LedgerService ledgerService;

    private Account sampleAccount;

    @BeforeEach
    void setUp() {
        sampleAccount = Account.builder()
                .id(UUID.randomUUID())
                .accountNumber("FINT-INR-9001")
                .ownerName("Bhavishya Pande")
                .balance(new BigDecimal("50000.00"))
                .version(1L)
                .build();
    }

    @Test
    void shouldDebitAccountSuccessfullyWhenSufficientBalanceExists() {
        when(accountRepository.findByAccountNumber("FINT-INR-9001")).thenReturn(Optional.of(sampleAccount));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        Transaction result = ledgerService.executeTransaction(
                "FINT-INR-9001", new BigDecimal("12500.00"), "DEBIT", "CLOUD_INFRA", "TX-2026-001"
        );

        assertNotNull(result);
        assertEquals(new BigDecimal("37500.00"), sampleAccount.getBalance());
        verify(accountRepository, times(1)).save(sampleAccount);
    }

    @Test
    void shouldThrowExceptionWhenDebitExceedsBalance() {
        when(accountRepository.findByAccountNumber("FINT-INR-9001")).thenReturn(Optional.of(sampleAccount));

        assertThrows(IllegalStateException.class, () -> ledgerService.executeTransaction(
                "FINT-INR-9001", new BigDecimal("75000.00"), "DEBIT", "EQUIPMENT", "TX-2026-002"
        ));

        verify(transactionRepository, never()).save(any());
    }
}
