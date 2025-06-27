package com.ajaysarwade.Treading.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class PaymentDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String accountNumber;
	
	private String accountHoldername;
	
	private String ifscCode;
	
	private String bankName;
	
	@OneToOne
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private User user;
	

}
