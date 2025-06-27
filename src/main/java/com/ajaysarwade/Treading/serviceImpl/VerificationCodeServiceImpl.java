package com.ajaysarwade.Treading.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajaysarwade.Treading.domain.VerificationType;
import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.model.VerifivationCode;
import com.ajaysarwade.Treading.repository.VerificationCodeRepository;
import com.ajaysarwade.Treading.service.VerificationCodeService;
import com.ajaysarwade.Treading.util.OtpUtils;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

	@Autowired
	private VerificationCodeRepository verificationCodeRepository;

	@Override
	public VerifivationCode sendVerificationCodeOtp(User user, VerificationType verificationType) {
		VerifivationCode newVerifivationCode = new VerifivationCode();
		newVerifivationCode.setOtp(OtpUtils.generateOTP());
		newVerifivationCode.setVerificationType(verificationType);
		newVerifivationCode.setUser(user);
		return verificationCodeRepository.save(newVerifivationCode);
	}

	@Override
	public VerifivationCode getVerificationCodeById(Long id) throws Exception {
		Optional< VerifivationCode> veriOptional= verificationCodeRepository.findById(id);
		if (veriOptional.isPresent()) {
			return veriOptional.get();
		}
		throw new Exception("Verification Code not found");
	}

	@Override
	public VerifivationCode getVerificationCodeByUser(Long userId) {
		
		return verificationCodeRepository.findByUserId(userId);
	}

	@Override
	public void deleteVerificationCodeById(VerifivationCode code) {
		verificationCodeRepository.delete(code);

	}

}
