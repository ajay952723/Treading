package com.ajaysarwade.Treading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajaysarwade.Treading.domain.PaymentMethod;
import com.ajaysarwade.Treading.model.PaymentOrder;
import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.response.PaymentResponse;
import com.ajaysarwade.Treading.service.PaymentService;
import com.ajaysarwade.Treading.service.UserService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PaymentService paymentService;
	
	@PostMapping("/{paymentMethod}/amount/{amount}")
	public ResponseEntity<PaymentResponse> paymentSuccessHandler(
			@PathVariable Long amount,
			@PathVariable PaymentMethod paymentMethod,
			@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userService.findProfileByJwt(jwt);
		
		PaymentResponse paymentResponse;
		
		PaymentOrder order = paymentService.createOrder(amount, paymentMethod, user);
		
		if (paymentMethod.equals(PaymentMethod.RAZORPAY)) {
			paymentResponse=paymentService.createRazorpayPaymentLink(user, amount,order.getId());
		}else {
			paymentResponse=paymentService.createStripePaymentLink(user, amount, order.getAmount());
		}
		return new ResponseEntity<>(paymentResponse,HttpStatus.CREATED);
	}

}
