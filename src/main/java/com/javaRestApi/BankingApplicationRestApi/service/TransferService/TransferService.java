

package com.javaRestApi.BankingApplicationRestApi.service.TransferService;

import com.javaRestApi.BankingApplicationRestApi.Execption.InsufficentAmount;
import com.javaRestApi.BankingApplicationRestApi.Execption.UserNotFound;
import com.javaRestApi.BankingApplicationRestApi.Model.AccountDTO.Account;
import com.javaRestApi.BankingApplicationRestApi.Model.TransactionDTO.Transaction;
import com.javaRestApi.BankingApplicationRestApi.Model.TransferAmount.TransferDTO;
import com.javaRestApi.BankingApplicationRestApi.Model.TransferAmount.TransferRequestDTO;
import com.javaRestApi.BankingApplicationRestApi.Repository.AccountRepositry;
import com.javaRestApi.BankingApplicationRestApi.Repository.TransactionalRepositry;
import com.javaRestApi.BankingApplicationRestApi.Repository.TransferRepositry;

import jakarta.transaction.Transactional;
import org.jetbrains.annotations.UnknownNullability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class TransferService {

    @Autowired
    private AccountRepositry repositry;

    @Autowired
    private TransferRepositry transferRepositry;

    @Autowired
    private TransactionalRepositry transactionalRepositry;

    public TransferDTO transfer(@UnknownNullability TransferRequestDTO requestDTO) {

        Account sender = repositry.findByAccountNumber(
                requestDTO.getFromAccountNumber()
        );

        Account receiver = repositry.findByAccountNumber(
                requestDTO.getToAccountNumber()
        );

        if (sender == null) {
            throw new UserNotFound("Sender Account does not exist");
        }

        if (receiver == null) {
            throw new UserNotFound("Receiver Account does not exist");
        }

        if (!"active".equalsIgnoreCase(sender.getStatus())) {
            throw new UserNotFound("Sender account is not active");
        }

        if (!"active".equalsIgnoreCase(receiver.getStatus())) {
            throw new UserNotFound("Receiver account is not active");
        }

        if (sender.getAccountNumber().equals(receiver.getAccountNumber())) {
            throw new IllegalArgumentException(
                    "Sender and receiver accounts cannot be the same"
            );
        }

        if (requestDTO.getAmount() <= 0) {
            throw new IllegalArgumentException(
                    "Transfer amount must be greater than zero"
            );
        }

        if (sender.getBalance() < requestDTO.getAmount()) {
            throw new InsufficentAmount(
                    "Your current balance is insufficient to transfer the amount"
            );
        }

        sender.setBalance(
                sender.getBalance() - requestDTO.getAmount()
        );

        receiver.setBalance(
                receiver.getBalance() + requestDTO.getAmount()
        );

        repositry.save(sender);
        repositry.save(receiver);

        LocalDateTime transactionTime = LocalDateTime.now();

        TransferDTO transferDTO = new TransferDTO();

        transferDTO.setTransactionId(UUID.randomUUID().toString());
        transferDTO.setFromAccountNumber(sender.getAccountNumber());
        transferDTO.setToAccountNumber(receiver.getAccountNumber());
        transferDTO.setAmount(requestDTO.getAmount());
        transferDTO.setSenderBalance(sender.getBalance());
        transferDTO.setReceiverBalance(receiver.getBalance());
        transferDTO.setStatus("SUCCESS");
        transferDTO.setMessage("Money transferred successfully");
        transferDTO.setTransactionDateTime(transactionTime);

        Transaction senderTransaction = new Transaction();

        senderTransaction.setAccountNumber(sender.getAccountNumber());
        senderTransaction.setTransactionType("TRANSFER_OUT");
        senderTransaction.setAmount(requestDTO.getAmount());
        senderTransaction.setBalanceAfterTransaction(sender.getBalance());
        senderTransaction.setReferenceAccount(receiver.getAccountNumber());
        senderTransaction.setStatus("SUCCESS");
        senderTransaction.setDescription("Money Transfer");
        senderTransaction.setTransactionDate(transactionTime);

        transactionalRepositry.save(senderTransaction);

        Transaction receiverTransaction = new Transaction();

        receiverTransaction.setAccountNumber(receiver.getAccountNumber());
        receiverTransaction.setTransactionType("TRANSFER_IN");
        receiverTransaction.setAmount(requestDTO.getAmount());
        receiverTransaction.setBalanceAfterTransaction(receiver.getBalance());
        receiverTransaction.setReferenceAccount(sender.getAccountNumber());
        receiverTransaction.setStatus("SUCCESS");
        receiverTransaction.setDescription("Money Received");
        receiverTransaction.setTransactionDate(transactionTime);

        transactionalRepositry.save(receiverTransaction);

        return transferDTO;
    }
}
