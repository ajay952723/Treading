package com.ajaysarwade.Treading.service;

import com.ajaysarwade.Treading.model.TwoFactorOtp;
import com.ajaysarwade.Treading.model.User;

public interface TwoFactorOtpService {

	TwoFactorOtp createFactorOtp(User user, String otp, String jwt);

	TwoFactorOtp findByUser(Long userId);

	TwoFactorOtp findById(String id);

	boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp, String otp);

	void deleteFactorOtp(TwoFactorOtp twoFactorOtp);

}
