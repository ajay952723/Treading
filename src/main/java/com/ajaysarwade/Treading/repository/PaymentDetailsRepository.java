package com.ajaysarwade.Treading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajaysarwade.Treading.model.PaymentDetails;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Long>{

	PaymentDetails findByUserId(Long userId);
}
