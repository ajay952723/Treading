package com.ajaysarwade.Treading.serviceImpl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ajaysarwade.Treading.domain.PaymentMethod;
import com.ajaysarwade.Treading.domain.PaymentOrderStatus;
import com.ajaysarwade.Treading.model.PaymentOrder;
import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.repository.PaymentOrderRepositroy;
import com.ajaysarwade.Treading.response.PaymentResponse;
import com.ajaysarwade.Treading.service.PaymentService;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentOrderRepositroy paymentOrderRepositroy;

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecret;

    private String stripeSecretKey = "stripeSecretKey";

    // 1. Create order entry in DB
    @Override
    public PaymentOrder createOrder(Long amount, PaymentMethod paymentMethod, User user) {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setUser(user);
        paymentOrder.setAmount(amount);
        paymentOrder.setPaymentMethod(paymentMethod);
        paymentOrder.setStatus(PaymentOrderStatus.PENDING);
        return paymentOrderRepositroy.save(paymentOrder);
    }

    // 2. Get order by ID
    @Override
    public PaymentOrder getPaymentOrderyId(Long id) throws Exception {
        return paymentOrderRepositroy.findById(id)
                .orElseThrow(() -> new Exception("Payment Order Not Found"));
    }

    // 3. Proceed payment after success
    @Override
    public Boolean proccedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {
        if (paymentOrder.getStatus() == null) {
            paymentOrder.setStatus(PaymentOrderStatus.PENDING);
        }

        if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
            if (paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)) {
                RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);
                Payment payment = razorpay.payments.fetch(paymentId);

                String status = payment.get("status"); // "captured", "failed", etc.

                if (status.equals("captured")) {
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                    paymentOrderRepositroy.save(paymentOrder);
                    return true;
                } else {
                    paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                    paymentOrderRepositroy.save(paymentOrder);
                    return false;
                }
            }

            // For Stripe or other methods (assumed successful after webhook or redirect)
            paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
            paymentOrderRepositroy.save(paymentOrder);
            return true;
        }

        return false;
    }

    // 4. Razorpay payment link creation
    @Override
    public PaymentResponse createRazorpayPaymentLink(User user, Long amount, Long orderId) throws RazorpayException {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be a positive number");
        }

        long amountInPaise = amount * 100;

        RazorpayClient razorpay = new RazorpayClient(apiKey, apiSecret);

        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount", amountInPaise);
        paymentLinkRequest.put("currency", "INR");

        JSONObject customer = new JSONObject();
        customer.put("name", user.getFullName());
        customer.put("email", user.getEmail());

        paymentLinkRequest.put("customer", customer);

        JSONObject notify = new JSONObject();
        notify.put("email", true);
        paymentLinkRequest.put("notify", notify);

        // Callback URL to frontend with orderId
        paymentLinkRequest.put("callback_url", "http://localhost:5173/wallet/" + orderId);
        paymentLinkRequest.put("callback_method", "get");

        PaymentLink link = razorpay.paymentLink.create(paymentLinkRequest);

        PaymentResponse response = new PaymentResponse();
        response.setPayment_url(link.get("short_url"));
        return response;
    }

    // 5. Stripe payment link creation
    @Override
    public PaymentResponse createStripePaymentLink(User user, Long amount, Long orderId) {
        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:5173/wallet/" + orderId)
                .setCancelUrl("http://localhost:5173/payment-cancel/" + orderId)
                .addLineItem(
                        SessionCreateParams.LineItem.builder().setQuantity(1L)
                                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("usd")
                                        .setUnitAmount(amount * 100)
                                        .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName("Ajju Payment - Order #" + orderId)
                                                .build())
                                        .build())
                                .build())
                .build();

        try {
            Session session = Session.create(params);
            PaymentResponse response = new PaymentResponse();
            response.setPayment_url(session.getUrl());
            return response;
        } catch (StripeException e) {
            throw new RuntimeException("Stripe payment session creation failed: " + e.getMessage());
        }
    }
}
