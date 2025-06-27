package com.ajaysarwade.Treading.serviceImpl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajaysarwade.Treading.model.TwoFactorOtp;
import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.repository.TwoFactorOtpRepository;
import com.ajaysarwade.Treading.service.TwoFactorOtpService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class TwoFactorOtpImpl  implements TwoFactorOtpService{
	
	@Autowired
	private final TwoFactorOtpRepository twoFactorOtpRepository;

	@Override
	public TwoFactorOtp createFactorOtp(User user, String otp, String jwt) {
		UUID uuid = UUID.randomUUID();
		String id= user.toString();
		
		TwoFactorOtp factorOtp = new TwoFactorOtp();
		factorOtp.setId(id);
		factorOtp.setJwt(jwt);
		factorOtp.setOtp(otp);
		factorOtp.setUser(user);
		
		return twoFactorOtpRepository.save(factorOtp);
	}

	@Override
	public TwoFactorOtp findByUser(Long userId) {
		
		return twoFactorOtpRepository.findByUserId(userId);
	}

	@Override
	public TwoFactorOtp findById(String id) {
		Optional< TwoFactorOtp> optional = twoFactorOtpRepository.findById(id);
		return optional.orElseThrow(null);
	}

	@Override
	public boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp, String otp) {
		
		return twoFactorOtp.getOtp().equals(otp);
	}

	@Override
	public void deleteFactorOtp(TwoFactorOtp twoFactorOtp) {
		twoFactorOtpRepository.delete(twoFactorOtp);
		
	}

}
