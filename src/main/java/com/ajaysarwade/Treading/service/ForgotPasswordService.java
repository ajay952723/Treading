package com.ajaysarwade.Treading.service;

import com.ajaysarwade.Treading.domain.VerificationType;
import com.ajaysarwade.Treading.model.ForgotPasswordToken;
import com.ajaysarwade.Treading.model.User;

public interface ForgotPasswordService {

    ForgotPasswordToken createToken(User user, String otp, VerificationType verificationType, String sendTo);

    ForgotPasswordToken findById(Long id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteToken(ForgotPasswordToken token);
}
