package com.ajaysarwade.Treading.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajaysarwade.Treading.model.PaymentDetails;
import com.ajaysarwade.Treading.model.PaymentOrder;

public interface PaymentOrderRepositroy  extends JpaRepository<PaymentOrder, Long>{

	
}
