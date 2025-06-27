package com.ajaysarwade.Treading.model;

import com.ajaysarwade.Treading.domain.VerificationType;

import lombok.Data;

@Data
public class TwoFactorAuth {

	private boolean isEnable = false;

	private VerificationType sendTo;
}
