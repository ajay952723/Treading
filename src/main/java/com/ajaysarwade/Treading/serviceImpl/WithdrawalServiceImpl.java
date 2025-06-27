package com.ajaysarwade.Treading.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajaysarwade.Treading.domain.WithDrawalStatus;
import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.model.Withdrawal;
import com.ajaysarwade.Treading.repository.WithdrawalRepository;
import com.ajaysarwade.Treading.service.WithdrawalService;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {

	@Autowired
	private WithdrawalRepository withdrawalRespository;

	@Override
	public Withdrawal requestWithdrawal(Long amount, User user) {
		System.out.println("Inside requestWithdrawal()");

		Withdrawal withdrawal = new Withdrawal();
		withdrawal.setAmount(amount);
		withdrawal.setUser(user);

		try {
			// Simulate bank payment immediately (or wallet deduction confirmation)
			boolean paymentSuccess = simulateBankTransfer(withdrawal);

			if (paymentSuccess) {
				withdrawal.setStatus(WithDrawalStatus.SUCCESS);
				System.out.println("Auto-payment succeeded. Status = SUCCESS");
			} else {
				withdrawal.setStatus(WithDrawalStatus.FAILED);
				System.out.println("Auto-payment failed. Status = FAILED");
			}

		} catch (Exception e) {
			System.out.println("⚠️ Exception during simulateBankTransfer: " + e.getMessage());
			withdrawal.setStatus(WithDrawalStatus.FAILED);
		}

		// Final safety: set default if status somehow not set
		if (withdrawal.getStatus() == null) {
			withdrawal.setStatus(WithDrawalStatus.PENDING);
			System.out.println("❌ Status was null. Set to PENDING as fallback.");
		}

		System.out.println("Final status before save: " + withdrawal.getStatus());

		withdrawal = withdrawalRespository.save(withdrawal);
		System.out.println("✅ Saved withdrawal with ID: " + withdrawal.getId() + ", Status: " + withdrawal.getStatus());

		return withdrawal;
	}

	@Override
	public Withdrawal procedWithdrawal(Long withdrawalId, boolean accept) throws Exception {
		System.out.println("Inside procedWithdrawal() for ID: " + withdrawalId + ", Accept: " + accept);

		Optional<Withdrawal> optionalWithdrawal = withdrawalRespository.findById(withdrawalId);

		if (optionalWithdrawal.isEmpty()) {
			System.out.println("Withdrawal not found for ID: " + withdrawalId);
			throw new Exception("Withdrawal not found");
		}

		Withdrawal withdrawal = optionalWithdrawal.get();

		if (accept) {
			System.out.println("Simulating payment transfer...");
			boolean paymentSuccess = simulateBankTransfer(withdrawal);

			if (paymentSuccess) {
				withdrawal.setStatus(WithDrawalStatus.SUCCESS);
				System.out.println("Payment succeeded. Setting status to SUCCESS");
			} else {
				withdrawal.setStatus(WithDrawalStatus.FAILED);
				System.out.println("Payment failed. Setting status to FAILED");
			}
		} else {
			withdrawal.setStatus(WithDrawalStatus.FAILED); // Rejected by admin
			System.out.println("Withdrawal manually rejected. Setting status to FAILED");
		}

		withdrawal = withdrawalRespository.save(withdrawal);
		System.out.println(
				"Saved updated withdrawal with ID: " + withdrawal.getId() + ", Status: " + withdrawal.getStatus());

		return withdrawal;
	}

	private boolean simulateBankTransfer(Withdrawal withdrawal) {
		// Simulated bank payment logic (replace with actual API call)
		return true; // Assume payment is always successful for now
	}

	@Override
	public List<Withdrawal> getUsersWithdrawalHistory(User user) {
		return withdrawalRespository.findByUserId(user.getId());
	}

	@Override
	public List<Withdrawal> getAllWithDrawalRequest() {
		return withdrawalRespository.findAll();
	}
}
