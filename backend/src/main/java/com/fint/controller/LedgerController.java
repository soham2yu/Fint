package com.fint.controller;

import com.fint.model.Transaction;
import com.fint.service.LedgerService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LedgerController {
    private final LedgerService ledgerService;

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TxRequest req) {
        Transaction tx = ledgerService.executeTransaction(
                req.getAccountNumber(), req.getAmount(), req.getType(), req.getCategory(), req.getReferenceCode()
        );
        return ResponseEntity.status(201).body(Map.of(
                "status", "COMPLETED",
                "referenceCode", tx.getReferenceCode(),
                "updatedBalance", tx.getAccount().getBalance(),
                "timestamp", tx.getTimestamp()
        ));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<List<Transaction>> getHistory(@PathVariable String accountNumber) {
        return ResponseEntity.ok(ledgerService.getHistory(accountNumber));
    }

    @Data
    public static class TxRequest {
        private String accountNumber;
        private BigDecimal amount;
        private String type;
        private String category;
        private String referenceCode;
    }
}
