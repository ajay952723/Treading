package com.ajaysarwade.Treading.model;

import java.time.LocalDate;

import com.ajaysarwade.Treading.domain.WalletTransactionType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class WalletTransaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private WalletTransactionType type;
	
	@ManyToOne
	private Wallet wallet;
	
	private LocalDate date;
	
	private String transferId;
	
	private String purpose;
	
	private Long amount;
}
