package com.ajaysarwade.Treading.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaysarwade.Treading.domain.WalletTransactionType;
import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.model.Wallet;
import com.ajaysarwade.Treading.model.WalletTransaction;
import com.ajaysarwade.Treading.model.Withdrawal;
import com.ajaysarwade.Treading.service.TransactionService;
import com.ajaysarwade.Treading.service.UserService;
import com.ajaysarwade.Treading.service.WalletService;
import com.ajaysarwade.Treading.service.WithdrawalService;

@RestController
@RequestMapping("/api/withdrawal")
public class WithdrawalController {

	@Autowired
	private WithdrawalService withdrawalService;

	@Autowired
	private WalletService walletService;

	@Autowired
	private UserService userService;

	@Autowired
	private TransactionService transactionService;

	@PostMapping("/{amount}")
	public ResponseEntity<?> withdrawalRequest(@PathVariable Long amount, @RequestHeader("Authorization") String jwt)
			throws Exception {
		User user = userService.findProfileByJwt(jwt);

		Wallet userWallet = walletService.getUserWallet(user);

		Withdrawal withdrawal = withdrawalService.requestWithdrawal(amount, user);
		walletService.addbalance(userWallet, -withdrawal.getAmount());
		
		WalletTransaction transaction = transactionService.createTransaction(
				userWallet,
				WalletTransactionType.WITHDRAWAL,null,"Bank Account withdrawal",
				withdrawal.getAmount());

		return new ResponseEntity<>(withdrawal, HttpStatus.OK);
	}

	@PatchMapping("/admin/withdrawal/{id}/proced/{accept}")
	public ResponseEntity<?> proceedWithdrawal(@PathVariable Long id, @RequestHeader("Authorization") String jwt,
	                                           @PathVariable boolean accept) throws Exception {
	    User user = userService.findProfileByJwt(jwt);

	    Withdrawal withdrawal = withdrawalService.procedWithdrawal(id, accept);

	    // Return updated withdrawal with correct status
	    return new ResponseEntity<>(withdrawal, HttpStatus.OK);
	}

	@GetMapping()
	public ResponseEntity<List<Withdrawal>> getWithdrawalHistory(

			@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findProfileByJwt(jwt);

		List<Withdrawal> withdrawal = withdrawalService.getUsersWithdrawalHistory(user);

		return new ResponseEntity<>(withdrawal, HttpStatus.OK);
	}

	@GetMapping("/admin/withdrawal")
	public ResponseEntity<List<Withdrawal>> getAllWithdrawalrequest(

			@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findProfileByJwt(jwt);

		List<Withdrawal> withdrawal = withdrawalService.getAllWithDrawalRequest();

		return new ResponseEntity<>(withdrawal, HttpStatus.OK);
	}

}
