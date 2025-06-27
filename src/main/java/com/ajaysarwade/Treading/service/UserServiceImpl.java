package com.ajaysarwade.Treading.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajaysarwade.Treading.config.JwtProvider;
import com.ajaysarwade.Treading.domain.VerificationType;
import com.ajaysarwade.Treading.model.TwoFactorAuth;
import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	 private UserRepository userRepository;

	@Override
	public User findProfileByJwt(String jwt) throws Exception {
		String email = JwtProvider.getEmailFromJwtToken(jwt);
		User user = userRepository.findByEmail(email);
		if (user==null) {
			throw new Exception("User not found");
		}
		return user;
	}

	@Override
	public User findUserByEmail(String email) throws Exception {
		
		User user = userRepository.findByEmail(email);
		if (user==null) {
			throw new Exception("User not found");
		}
		return user;
	}

	@Override
	public User findUserById(Long userId) throws Exception {
		
		Optional<User> user = userRepository.findById(userId);
		if (user.isEmpty()) {
			throw new Exception("User not found");
		}
		return user.get();
	}

	

	@Override
	public User UdatePassword(User user, String newPassword) {
		user.setPassword(newPassword);
		return userRepository.save(user);
	}

	@Override
	public User enabletwoFactorAuthentication(VerificationType verificationType, String sendTo, User user) {
		TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
		twoFactorAuth.setEnable(true);
		twoFactorAuth.setSendTo(verificationType);
		
		user.setTwoFactorAuth(twoFactorAuth);
		
		return userRepository.save(user);
	}

}
