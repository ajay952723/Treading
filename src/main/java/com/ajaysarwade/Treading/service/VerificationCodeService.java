package com.ajaysarwade.Treading.service;

import com.ajaysarwade.Treading.domain.VerificationType;
import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.model.VerifivationCode;

public interface VerificationCodeService {

	VerifivationCode sendVerificationCodeOtp(User user, VerificationType verificationType);

	VerifivationCode getVerificationCodeById(Long id) throws Exception;

	VerifivationCode getVerificationCodeByUser(Long userId);

	void deleteVerificationCodeById(VerifivationCode code);
	


}
