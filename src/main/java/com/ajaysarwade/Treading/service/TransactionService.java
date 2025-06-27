package com.ajaysarwade.Treading.service;

import java.util.List;

import com.ajaysarwade.Treading.domain.WalletTransactionType;
import com.ajaysarwade.Treading.model.Wallet;
import com.ajaysarwade.Treading.model.WalletTransaction;

public interface TransactionService {

	List<WalletTransaction> getTransactionByWallet(Wallet wallet);

	WalletTransaction createTransaction(Wallet userWallet, WalletTransactionType withdrawal, Object object,
			String string, Long amount);

}
