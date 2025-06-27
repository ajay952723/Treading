package com.ajaysarwade.Treading.service;

import java.util.List;

import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.model.Withdrawal;

public interface WithdrawalService {

	Withdrawal requestWithdrawal(Long amount, User user);

	Withdrawal procedWithdrawal(Long withdrawalId,boolean accept) throws Exception;
	
	List<Withdrawal> getUsersWithdrawalHistory(User user);
	
	List<Withdrawal> getAllWithDrawalRequest();

}
