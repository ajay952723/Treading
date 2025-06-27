package com.ajaysarwade.Treading.service;

import com.ajaysarwade.Treading.model.Order;
import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.model.Wallet;

public interface WalletService {

	Wallet getUserWallet(User user);

	Wallet addbalance(Wallet wallet, Long amount);

	Wallet findWalletById(Long id) throws Exception;

	Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws Exception;

	Wallet payOrderPayment(Order order, User user) throws Exception;

}
