package com.ajaysarwade.Treading.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajaysarwade.Treading.model.PaymentDetails;
import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.repository.PaymentDetailsRepository;
import com.ajaysarwade.Treading.service.PaymentDetailsService;

@Service
public class PaymentDetailsServiceImpl implements PaymentDetailsService {

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Override
    public PaymentDetails addpaymentDetails(String accountNumber, String accountHoldername,
                                            String ifscCode, String bankName, User user) {

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setAccountNumber(accountNumber);
        paymentDetails.setAccountHoldername(accountHoldername);
        paymentDetails.setIfscCode(ifscCode);
        paymentDetails.setBankName(bankName);
        paymentDetails.setUser(user);

        return paymentDetailsRepository.save(paymentDetails);
    }

    @Override
    public PaymentDetails getUserPaymentDetails(User user) {
        return paymentDetailsRepository.findByUserId(user.getId());
    }
}
