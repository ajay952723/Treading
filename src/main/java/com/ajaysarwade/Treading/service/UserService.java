package com.ajaysarwade.Treading.service;

import com.ajaysarwade.Treading.domain.VerificationType;
import com.ajaysarwade.Treading.model.User;

public interface UserService {

	public User findProfileByJwt(String jwt) throws Exception;

	public User findUserByEmail(String email) throws Exception ;

	public User findUserById(Long userId) throws Exception;

	public User enabletwoFactorAuthentication(VerificationType verificationType, String sendTo, User user);

	User UdatePassword(User user, String newPassword);
}
