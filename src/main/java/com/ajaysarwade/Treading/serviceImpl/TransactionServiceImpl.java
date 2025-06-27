package com.ajaysarwade.Treading.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajaysarwade.Treading.domain.WalletTransactionType;
import com.ajaysarwade.Treading.model.Wallet;
import com.ajaysarwade.Treading.model.WalletTransaction;
import com.ajaysarwade.Treading.repository.TransactionRepository;
import com.ajaysarwade.Treading.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<WalletTransaction> getTransactionByWallet(Wallet wallet) {
        return transactionRepository.findByWallet(wallet);
    }

    @Override
    public WalletTransaction createTransaction(Wallet wallet,
                                               WalletTransactionType type,
                                               Object unused,  // You can remove this if not used
                                               String purpose,
                                               Long amount) {

        WalletTransaction transaction = new WalletTransaction();
        transaction.setWallet(wallet);
        transaction.setType(type);
        transaction.setPurpose(purpose);
        transaction.setAmount(amount);
        transaction.setDate(LocalDate.now());
        transaction.setTransferId(UUID.randomUUID().toString());

        return transactionRepository.save(transaction);
    }
}
