package com.ajaysarwade.Treading.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ajaysarwade.Treading.model.Order;
import com.ajaysarwade.Treading.model.PaymentOrder;
import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.model.Wallet;
import com.ajaysarwade.Treading.model.WalletTransaction;
import com.ajaysarwade.Treading.service.OrderService;
import com.ajaysarwade.Treading.service.PaymentService;
import com.ajaysarwade.Treading.service.UserService;
import com.ajaysarwade.Treading.service.WalletService;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    // 1. Get wallet by user
    @GetMapping("/user/wallet")
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);
        return ResponseEntity.ok(wallet);
    }

    // 2. Pay for order (BUY/SELL)
    @PutMapping("/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId) throws Exception {
        User user = userService.findProfileByJwt(jwt);
        Order order = orderService.getOrderById(orderId);
        Wallet updatedWallet = walletService.payOrderPayment(order, user);
        return ResponseEntity.ok(updatedWallet);
    }

    // 3. Get wallet by ID
    @GetMapping("/{walletId}")
    public ResponseEntity<Wallet> getWalletById(@PathVariable Long walletId) throws Exception {
        Wallet wallet = walletService.findWalletById(walletId);
        return ResponseEntity.ok(wallet);
    }

    // 4. Wallet-to-wallet transfer
    @PutMapping("/{walletId}/transfer")
    public ResponseEntity<Wallet> transferBalance(@RequestHeader("Authorization") String jwt,
            @PathVariable Long walletId, @RequestBody WalletTransaction req) throws Exception {

        User sender = userService.findProfileByJwt(jwt);
        Wallet receiverWallet = walletService.findWalletById(walletId);
        Wallet updatedSenderWallet = walletService.walletToWalletTransfer(sender, receiverWallet, req.getAmount());

        return ResponseEntity.ok(updatedSenderWallet);
    }

    // 5. Add balance to wallet using Razorpay
    @PostMapping("/order/{orderId}/pay-order")
    public ResponseEntity<Wallet> addBalanceToWallet(  @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId,
            @RequestParam(name = "payment_id") String paymentId) throws Exception {

        User user = userService.findProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);
        PaymentOrder order = paymentService.getPaymentOrderyId(orderId);

        boolean status = paymentService.proccedPaymentOrder(order, paymentId);
        if (status) {
            wallet = walletService.addbalance(wallet, order.getAmount());
        }

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }
}
