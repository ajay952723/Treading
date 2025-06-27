package com.ajaysarwade.Treading.serviceImpl;

import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
	
	private JavaMailSender javaMailSender;
	
	public void sendVerifactionOtpEmail(String email,String otp) throws MessagingException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,"utf-8");
		
		String subject="Verified OTP";
		
		String text="Your Verification Code is " +otp;
		
		helper.setSubject(subject);
		helper.setText(text);
		helper.setTo(email);
		
		try {
			javaMailSender.send(message);
		} catch (Exception e) {
			throw new MailSendException(e.getMessage());
		}
	}

}
