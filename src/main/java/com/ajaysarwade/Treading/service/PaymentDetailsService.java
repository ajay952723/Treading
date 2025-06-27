package com.ajaysarwade.Treading.service;

import com.ajaysarwade.Treading.model.PaymentDetails;
import com.ajaysarwade.Treading.model.User;

public interface PaymentDetailsService {

    PaymentDetails addpaymentDetails(String accountNumber, String accountHoldername,
                                     String ifscCode, String bankName, User user);

    PaymentDetails getUserPaymentDetails(User user);
}
