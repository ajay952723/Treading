package com.ajaysarwade.Treading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajaysarwade.Treading.model.ForgotPasswordToken;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordToken, Long>{
	ForgotPasswordToken findByUserId(Long userId);
}
