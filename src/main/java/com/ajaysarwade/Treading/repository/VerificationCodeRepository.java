package com.ajaysarwade.Treading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajaysarwade.Treading.model.VerifivationCode;

public interface VerificationCodeRepository extends JpaRepository<VerifivationCode, Long>{
	
	VerifivationCode findByUserId(Long userId);

}
