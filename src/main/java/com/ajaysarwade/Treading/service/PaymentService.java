package com.ajaysarwade.Treading.service;

import com.ajaysarwade.Treading.domain.PaymentMethod;
import com.ajaysarwade.Treading.model.PaymentOrder;
import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.response.PaymentResponse;
import com.razorpay.RazorpayException;

public interface PaymentService {
	
	PaymentOrder createOrder(Long amount, PaymentMethod paymentMethod , User user);
	
	PaymentOrder getPaymentOrderyId(Long id) throws Exception;
	
	Boolean proccedPaymentOrder(PaymentOrder paymentOrder,String paymentId) throws RazorpayException;
	
	PaymentResponse createRazorpayPaymentLink(User user , Long amount,Long orderId) throws RazorpayException;
	
	PaymentResponse createStripePaymentLink(User user , Long amount,Long orderId);

}
