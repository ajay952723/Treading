package com.ajaysarwade.Treading.request;

import com.ajaysarwade.Treading.domain.VerificationType;

import lombok.Data;

@Data
public class ForgotPasswordTokenRequest {
	
	private String sendTo;
	private VerificationType type;
	
	

}
