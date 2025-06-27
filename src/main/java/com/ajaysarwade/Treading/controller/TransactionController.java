package com.ajaysarwade.Treading.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.model.Wallet;
import com.ajaysarwade.Treading.model.WalletTransaction;
import com.ajaysarwade.Treading.service.TransactionService;
import com.ajaysarwade.Treading.service.UserService;
import com.ajaysarwade.Treading.service.WalletService;


@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private WalletService walletService;
	
	@Autowired
	private TransactionService transactionService;
	
	@GetMapping()
	public ResponseEntity<List<WalletTransaction>> getUserWallet(
			@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userService.findProfileByJwt(jwt);
		
		Wallet wallet = walletService.getUserWallet(user);
		
		List<WalletTransaction> transactions = transactionService
				.getTransactionByWallet(wallet);
		return new ResponseEntity<>(transactions,HttpStatus.OK);
				
	}

}
