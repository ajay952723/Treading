package com.ajaysarwade.Treading.serviceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajaysarwade.Treading.domain.OrderType;
import com.ajaysarwade.Treading.model.Order;
import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.model.Wallet;
import com.ajaysarwade.Treading.repository.WalletRepository;
import com.ajaysarwade.Treading.service.WalletService;

@Service
public class WalletServiceImpl implements WalletService{
	
	@Autowired
	private WalletRepository walletRepository;

	@Override
	public Wallet getUserWallet(User user) {
	    // ✅ Corrected method name
	    Wallet wallet = walletRepository.findByUserId(user.getId());

	    if (wallet == null) {
	        wallet = new Wallet();
	        wallet.setUser(user);
	        wallet.setBalance(BigDecimal.ZERO);
	        wallet = walletRepository.save(wallet);
	    }

	    return wallet;
	}

	@Override
	public Wallet addbalance(Wallet wallet, Long amount) {
		BigDecimal balance = wallet.getBalance();
		BigDecimal newBalance= balance.add(BigDecimal.valueOf(amount));
		
		wallet.setBalance(newBalance);
		return walletRepository.save(wallet);
	}

	@Override
	public Wallet findWalletById(Long id) throws Exception {
		Optional<Wallet> optional = walletRepository.findById(id);
		if (optional.isPresent()) {
			return optional.get();
		}
		throw new Exception("Wallet not found");
	}

	@Override
	public Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws Exception {
		Wallet senderWallet = getUserWallet(sender);
		if (senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount))<0) {
			throw new Exception("Insufficient Balance....!!!");
		}
		
		BigDecimal senderBalance = senderWallet.getBalance().subtract(BigDecimal.valueOf(amount));
		senderWallet.setBalance(senderBalance);
		walletRepository.save(senderWallet);
		
		BigDecimal receiverBalance = receiverWallet.getBalance().add(BigDecimal.valueOf(amount));
		receiverWallet.setBalance(receiverBalance);
		walletRepository.save(receiverWallet);
		
		return senderWallet;
	}

	@Override
	public Wallet payOrderPayment(Order order, User user) throws Exception {
		Wallet wallet = getUserWallet(user);
		if (order.getOrderType().equals(OrderType.BUY)) {
			BigDecimal newBalance= wallet.getBalance().subtract(order.getPrice());
			if (newBalance.compareTo(order.getPrice())<0) {
				throw new Exception("Insuffiecnt Balance For this transactions");
			}
			wallet.setBalance(newBalance);
		}else {
			BigDecimal newbalance= wallet.getBalance().add(order.getPrice());
			wallet.setBalance(newbalance);
		}
		walletRepository.save(wallet);
		return wallet;
	}

}
