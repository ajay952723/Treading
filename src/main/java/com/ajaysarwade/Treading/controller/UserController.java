package com.ajaysarwade.Treading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ajaysarwade.Treading.domain.VerificationType;
import com.ajaysarwade.Treading.model.ForgotPasswordToken;
import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.model.VerifivationCode;
import com.ajaysarwade.Treading.request.ForgotPasswordTokenRequest;
import com.ajaysarwade.Treading.request.ResetPasswordRequest;
import com.ajaysarwade.Treading.response.ApiResponse;
import com.ajaysarwade.Treading.response.AuthResponse;
import com.ajaysarwade.Treading.service.ForgotPasswordService;
import com.ajaysarwade.Treading.service.UserService;
import com.ajaysarwade.Treading.service.VerificationCodeService;
import com.ajaysarwade.Treading.serviceImpl.EmailService;
import com.ajaysarwade.Treading.util.OtpUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final EmailService emailService;

    @Autowired
    private final VerificationCodeService verificationCodeService;

    @Autowired
    private final ForgotPasswordService forgotPasswordService;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findProfileByJwt(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(@RequestHeader("Authorization") String jwt,
                                                      @PathVariable VerificationType verificationType) throws Exception {
        User user = userService.findProfileByJwt(jwt);

        VerifivationCode verifivationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
        if (verifivationCode == null) {
            verifivationCode = verificationCodeService.sendVerificationCodeOtp(user, verificationType);
        }

        if (verificationType.equals(VerificationType.EMAIL)) {
            emailService.sendVerifactionOtpEmail(user.getEmail(), verifivationCode.getOtp());
        }

        return new ResponseEntity<>("Verification OTP successfully sent", HttpStatus.OK);
    }

    @PatchMapping("/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(@RequestHeader("Authorization") String jwt,
                                                              @PathVariable String otp) throws Exception {
        User user = userService.findProfileByJwt(jwt);

        VerifivationCode verifivationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
        String sendTo = verifivationCode.getVerificationType().equals(VerificationType.EMAIL)
                ? verifivationCode.getEmail()
                : verifivationCode.getMobile();

        boolean isVerified = verifivationCode.getOtp().equals(otp);

        if (isVerified) {
            User updatedUser = userService.enabletwoFactorAuthentication(
                    verifivationCode.getVerificationType(), sendTo, user);
            verificationCodeService.deleteVerificationCodeById(verifivationCode);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }

        throw new Exception("Wrong OTP");
    }

    @PostMapping("/auth/user/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp(@RequestBody ForgotPasswordTokenRequest req) throws Exception {
        User user = userService.findUserByEmail(req.getSendTo());
        String otp = OtpUtils.generateOTP();

        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findByUser(user.getId());

        if (forgotPasswordToken == null) {
            forgotPasswordToken = forgotPasswordService.createToken(user, otp, req.getType(), req.getSendTo());
        }

        if (req.getType().equals(VerificationType.EMAIL)) {
            emailService.sendVerifactionOtpEmail(user.getEmail(), forgotPasswordToken.getOtp());
        }

        AuthResponse response = new AuthResponse();
        response.setSession(forgotPasswordToken.getId().toString());
        response.setMessage("Reset OTP sent successfully");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/auth/users/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestHeader("Authorization") String jwt,
                                                     @RequestBody ResetPasswordRequest req,
                                                     @RequestParam String id) throws Exception {
        User user = userService.findProfileByJwt(jwt);

        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findById(Long.parseLong(id));

        boolean isVerified = forgotPasswordToken.getOtp().equals(req.getOtp());

        if (isVerified) {
            userService.UdatePassword(forgotPasswordToken.getUser(), req.getPassword());
            ApiResponse response = new ApiResponse();
            response.setMessage("Password updated successfully");
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }

        throw new Exception("Wrong OTP");
    }
}
