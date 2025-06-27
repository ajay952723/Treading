package com.ajaysarwade.Treading.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajaysarwade.Treading.domain.OrderType;
import com.ajaysarwade.Treading.model.CoinMarketData;
import com.ajaysarwade.Treading.model.Order;
import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.request.CreateOrderRequest;
import com.ajaysarwade.Treading.service.CoinMarketService;
import com.ajaysarwade.Treading.service.OrderService;
import com.ajaysarwade.Treading.service.UserService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@Autowired
	private CoinMarketService coinService;

//	@Autowired
//	private WalletTransactionalService walletTransactionService;

	@PostMapping("/pay")
	public ResponseEntity<Order> payOrderPayment(@RequestHeader("Authorization") String jwt,
			@RequestBody CreateOrderRequest req) throws Exception {
		User user = userService.findProfileByJwt(jwt);
		CoinMarketData coinMarketData = coinService.findById(req.getCoinId());

		Order order = orderService.processOrder(coinMarketData, req.getQuantity(), req.getOrderType(), user);
		return ResponseEntity.ok(order);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<Order> getOrderById(@RequestHeader("Authorization") String jwt, @PathVariable Long orderId) throws Exception{
		User user = userService.findProfileByJwt(jwt);

		Order order = orderService.getOrderById(orderId);
		return ResponseEntity.ok(order);
	}

	@GetMapping("/all")
	public ResponseEntity<List<Order>> getAllOrderById(@RequestHeader("Authorization") String jwt,
			@RequestParam(required = false) OrderType order_type, @RequestParam(required = false) String assetSymbol) throws Exception{
		User userId = userService.findProfileByJwt(jwt);

		List<Order> order = orderService.getAllOrderOfUser(userId, order_type, assetSymbol);
		return ResponseEntity.ok(order);
	}

}
