package com.ajaysarwade.Treading.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajaysarwade.Treading.model.Wallet;
import com.ajaysarwade.Treading.model.WalletTransaction;

public interface TransactionRepository extends JpaRepository<WalletTransaction, Long> {
    List<WalletTransaction> findByWallet(Wallet wallet);
}
