package com.javaRestApi.BankingApplicationRestApi.Controller.Offline;

import com.javaRestApi.BankingApplicationRestApi.Model.AccountDTO.Account;
import com.javaRestApi.BankingApplicationRestApi.Model.TransactionDTO.Transaction;
import com.javaRestApi.BankingApplicationRestApi.Repository.AccountRepositry;
import com.javaRestApi.BankingApplicationRestApi.Repository.TransactionalRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/offline")
@CrossOrigin(origins = "*")
public class OfflineController {

    @Autowired
    private AccountRepositry accountRepositry;

    @Autowired
    private TransactionalRepositry transactionalRepositry;

    /**
     * Reserve funds from bank account for BlueTap offline Bluetooth wallet
     */
    @PostMapping("/reserve")
    @Transactional
    public ResponseEntity<Map<String, Object>> reserveFunds(@RequestBody Map<String, Object> request) {
        String accountNumber = (String) request.get("accountNumber");
        double amount = Double.parseDouble(request.get("amount").toString());

        Map<String, Object> response = new HashMap<>();
        Account account = accountRepositry.findByAccountNumber(accountNumber);

        if (account == null) {
            response.put("error", true);
            response.put("message", "Account not found: " + accountNumber);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (amount <= 0) {
            response.put("error", true);
            response.put("message", "Amount must be greater than zero");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (account.getBalance() < amount) {
            response.put("error", true);
            response.put("message", "Insufficient account balance to reserve offline funds");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Deduct from online balance into offline reserve
        account.setBalance(account.getBalance() - amount);
        accountRepositry.save(account);

        // Record transaction log
        Transaction txn = new Transaction();
        txn.setAccountNumber(account.getAccountNumber());
        txn.setTransactionType("OFFLINE_RESERVE");
        txn.setAmount(amount);
        txn.setBalanceAfterTransaction(account.getBalance());
        txn.setReferenceAccount("BLUETAP-OFFLINE-VAULT");
        txn.setStatus("SUCCESS");
        txn.setDescription("BlueTap Offline Token Allocation: Reserved ₹" + amount);
        txn.setTransactionDate(LocalDateTime.now());
        transactionalRepositry.save(txn);

        response.put("error", false);
        response.put("statusCode", 200);
        response.put("message", "Offline funds reserved successfully");
        response.put("accountNumber", account.getAccountNumber());
        response.put("reservedAmount", amount);
        response.put("newBalance", account.getBalance());
        response.put("tokenExpiryHours", 72);
        response.put("offlineTokenId", "BT-RES-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Settle offline Bluetooth payment voucher when device comes back online
     */
    @PostMapping("/settle")
    @Transactional
    public ResponseEntity<Map<String, Object>> settlePayment(@RequestBody Map<String, Object> request) {
        String fromAccountNum = (String) request.get("fromAccountNumber");
        String toAccountNum = (String) request.get("toAccountNumber");
        double amount = Double.parseDouble(request.get("amount").toString());
        String tokenId = (String) request.getOrDefault("tokenId", "BT-" + UUID.randomUUID().toString().substring(0, 8));
        String merchantName = (String) request.getOrDefault("merchantName", "BlueTap Merchant");
        boolean alreadyReserved = Boolean.parseBoolean(String.valueOf(request.getOrDefault("alreadyReserved", "true")));

        Map<String, Object> response = new HashMap<>();

        Account receiver = accountRepositry.findByAccountNumber(toAccountNum);
        if (receiver == null) {
            // If specific receiver doesn't exist, fallback to ACC100002 or first active merchant
            receiver = accountRepositry.findByAccountNumber("ACC100002");
            if (receiver == null) {
                receiver = accountRepositry.findAll().stream()
                        .filter(a -> !a.getAccountNumber().equals(fromAccountNum))
                        .findFirst().orElse(null);
            }
        }

        Account sender = accountRepositry.findByAccountNumber(fromAccountNum);

        if (sender != null && !alreadyReserved) {
            if (sender.getBalance() < amount) {
                response.put("error", true);
                response.put("message", "Insufficient funds for settlement");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            sender.setBalance(sender.getBalance() - amount);
            accountRepositry.save(sender);

            Transaction senderTxn = new Transaction();
            senderTxn.setAccountNumber(sender.getAccountNumber());
            senderTxn.setTransactionType("OFFLINE_BLE_SETTLE_DEBIT");
            senderTxn.setAmount(amount);
            senderTxn.setBalanceAfterTransaction(sender.getBalance());
            senderTxn.setReferenceAccount(receiver != null ? receiver.getAccountNumber() : "MERCHANT");
            senderTxn.setStatus("SUCCESS");
            senderTxn.setDescription("Offline Bluetooth Payment to " + merchantName + " [Token: " + tokenId + "]");
            senderTxn.setTransactionDate(LocalDateTime.now());
            transactionalRepositry.save(senderTxn);
        }

        if (receiver != null) {
            receiver.setBalance(receiver.getBalance() + amount);
            accountRepositry.save(receiver);

            Transaction recvTxn = new Transaction();
            recvTxn.setAccountNumber(receiver.getAccountNumber());
            recvTxn.setTransactionType("OFFLINE_BLE_SETTLE_CREDIT");
            recvTxn.setAmount(amount);
            recvTxn.setBalanceAfterTransaction(receiver.getBalance());
            recvTxn.setReferenceAccount(fromAccountNum);
            recvTxn.setStatus("SUCCESS");
            recvTxn.setDescription("Settled Offline Bluetooth Payment from " + fromAccountNum + " [Token: " + tokenId + "]");
            recvTxn.setTransactionDate(LocalDateTime.now());
            transactionalRepositry.save(recvTxn);
        }

        response.put("error", false);
        response.put("statusCode", 200);
        response.put("status", "SETTLED");
        response.put("message", "Offline Bluetooth voucher settled into banking ledger");
        response.put("tokenId", tokenId);
        response.put("amount", amount);
        response.put("fromAccount", fromAccountNum);
        response.put("toAccount", receiver != null ? receiver.getAccountNumber() : toAccountNum);
        response.put("settledAt", LocalDateTime.now().toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Return unused offline funds back to main bank account
     */
    @PostMapping("/return")
    @Transactional
    public ResponseEntity<Map<String, Object>> returnFunds(@RequestBody Map<String, Object> request) {
        String accountNumber = (String) request.get("accountNumber");
        double amount = Double.parseDouble(request.get("amount").toString());

        Map<String, Object> response = new HashMap<>();
        Account account = accountRepositry.findByAccountNumber(accountNumber);

        if (account == null) {
            response.put("error", true);
            response.put("message", "Account not found: " + accountNumber);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        account.setBalance(account.getBalance() + amount);
        accountRepositry.save(account);

        Transaction txn = new Transaction();
        txn.setAccountNumber(account.getAccountNumber());
        txn.setTransactionType("OFFLINE_REFUND_MAIN");
        txn.setAmount(amount);
        txn.setBalanceAfterTransaction(account.getBalance());
        txn.setReferenceAccount("BLUETAP-OFFLINE-VAULT");
        txn.setStatus("SUCCESS");
        txn.setDescription("Unused Offline Token Refunded to Bank Account");
        txn.setTransactionDate(LocalDateTime.now());
        transactionalRepositry.save(txn);

        response.put("error", false);
        response.put("statusCode", 200);
        response.put("message", "Unused offline balance returned to main account");
        response.put("newBalance", account.getBalance());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * List all accounts for demo selector
     */
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> listAccounts() {
        return new ResponseEntity<>(accountRepositry.findAll(), HttpStatus.OK);
    }
}
