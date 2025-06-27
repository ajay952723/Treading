package com.ajaysarwade.Treading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.ajaysarwade.Treading.config.JwtProvider;
import com.ajaysarwade.Treading.model.TwoFactorOtp;
import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.repository.UserRepository;
import com.ajaysarwade.Treading.response.AuthResponse;
import com.ajaysarwade.Treading.service.TwoFactorOtpService;
import com.ajaysarwade.Treading.service.WatchListService;
import com.ajaysarwade.Treading.serviceImpl.CustomeUserDeatilsService;
import com.ajaysarwade.Treading.serviceImpl.EmailService;
import com.ajaysarwade.Treading.util.OtpUtils;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TwoFactorOtpService factorOtpService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private WatchListService watchListService;

	@Autowired
	private CustomeUserDeatilsService customeUserDeatilsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception {

		User existingUser = userRepository.findByEmail(user.getEmail());
		if (existingUser != null) {
			throw new Exception("Email is already used with another account");
		}

		User newUser = new User();
		newUser.setFullName(user.getFullName());
		newUser.setEmail(user.getEmail());
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		newUser.setMobile(user.getMobile());

		User savedUser= userRepository.save(newUser);
		
		watchListService.createWathcList(savedUser);

		Authentication auth = authenticate(user.getEmail(), user.getPassword()); // ✅ fix
		SecurityContextHolder.getContext().setAuthentication(auth);
		String jwt = JwtProvider.generateToken(auth); // ✅ fix

		AuthResponse response = new AuthResponse();
		response.setJwt(jwt);
		response.setStatus(true);
		response.setMessage("Register Success");

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception {

		String username = user.getEmail();
		String password = user.getPassword();

		Authentication auth = authenticate(username, password);
		SecurityContextHolder.getContext().setAuthentication(auth);

		String jwt = JwtProvider.generateToken(auth);

		User authuser2 = userRepository.findByEmail(username);

		if (user.getTwoFactorAuth().isEnable()) {
			AuthResponse response = new AuthResponse();
			response.setMessage("Two Factor is enable");
			response.setTwoFactorAuthEnabled(true);
			String otp = OtpUtils.generateOTP();

			TwoFactorOtp oldTwoFactorOtp = factorOtpService.findByUser(authuser2.getId());
			if (oldTwoFactorOtp != null) {
				factorOtpService.deleteFactorOtp(oldTwoFactorOtp);

			}
			TwoFactorOtp newTwoFactorOtp = factorOtpService.
					createFactorOtp(authuser2, otp, jwt);
			emailService.sendVerifactionOtpEmail(username, otp);
			
			response.setSession(newTwoFactorOtp.getId());
			return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
		}

		AuthResponse response = new AuthResponse();
		response.setJwt(jwt);
		response.setStatus(true);
		response.setMessage("Login Success");

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private Authentication authenticate(String username, String password) {
		System.out.println("AUTHENTICATING: " + username);

		UserDetails userDetails = customeUserDeatilsService.loadUserByUsername(username);

		if (userDetails == null) {
			throw new BadCredentialsException("Invalid username");
		}

		System.out.println("Stored Encoded Password: " + userDetails.getPassword());
		System.out.println("Raw Password: " + password);

		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid password");
		}

		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
	
	@PostMapping("/two-factor/otp/{otp}")
	public ResponseEntity<AuthResponse> verifySiginOtp(@PathVariable String otp,
			@RequestParam String id) throws Exception{
		TwoFactorOtp twoFactorOtp = factorOtpService.findById(id);
		if (factorOtpService.verifyTwoFactorOtp(twoFactorOtp, otp)) {
			AuthResponse response = new AuthResponse();
			response.setMessage("Two Factor Authentication Verified");
			response.setTwoFactorAuthEnabled(true);
			response.setJwt(twoFactorOtp.getJwt());
			return new ResponseEntity<>(response,HttpStatus.OK);
		}
		
		throw new Exception("Invalied Otp");
	}
}
