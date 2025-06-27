package com.ajaysarwade.Treading.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajaysarwade.Treading.domain.OrderStatus;
import com.ajaysarwade.Treading.domain.OrderType;
import com.ajaysarwade.Treading.model.AssetModel;
import com.ajaysarwade.Treading.model.CoinMarketData;
import com.ajaysarwade.Treading.model.Order;
import com.ajaysarwade.Treading.model.OrderItem;
import com.ajaysarwade.Treading.model.User;
import com.ajaysarwade.Treading.repository.OrderItemRepository;
import com.ajaysarwade.Treading.repository.OrderRepository;
import com.ajaysarwade.Treading.service.AssetService;
import com.ajaysarwade.Treading.service.OrderService;
import com.ajaysarwade.Treading.service.WalletService;

import jakarta.transaction.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AssetService assetService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Override
    public Order createOrder(User user, OrderItem orderItem, OrderType orderType) {
        double price = orderItem.getCoin().getCurrentPrice() * orderItem.getQuantity();

        Order order = new Order();
        order.setUser(user);
        order.setOrderItem(orderItem);
        order.setOrderType(orderType);
        order.setPrice(BigDecimal.valueOf(price));
        order.setTimestamp(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);

        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(Long orderId) throws Exception {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new Exception("Order not found with id: " + orderId));
    }

    private OrderItem createOrderItem(CoinMarketData coin, double quantity, double buyPrice, double sellPrice) {
        OrderItem item = new OrderItem();
        item.setCoin(coin);
        item.setQuantity(quantity);
        item.setBuyprice(buyPrice);
        item.setSellPrice(sellPrice);
        return orderItemRepository.save(item);
    }

    @Transactional
    public Order buyAsset(CoinMarketData coin, double quantity, User user) throws Exception {
        if (quantity <= 0) {
            throw new Exception("Quantity should be > 0");
        }

        double buyPrice = coin.getCurrentPrice();
        OrderItem item = createOrderItem(coin, quantity, buyPrice, 0);
        Order order = createOrder(user, item, OrderType.BUY);
        item.setOrder(order);

        walletService.payOrderPayment(order, user);
        order.setOrderStatus(OrderStatus.SUCCESS);
        order.setOrderType(OrderType.BUY);

        Order savedOrder = orderRepository.save(order);

        AssetModel oldAsset = assetService.findAssetByUserIdAndCoinId(user.getId(), coin.getId());
        if (oldAsset == null) {
            assetService.createAsset(user, item.getCoin(), item.getQuantity());
        } else {
            assetService.updateAsset(oldAsset.getId(), quantity);
        }

        return savedOrder;
    }

    @Transactional
    public Order sellAsset(CoinMarketData coin, double quantity, User user) throws Exception {
        if (quantity <= 0) {
            throw new Exception("Quantity should be greater than 0");
        }

        double sellPrice = coin.getCurrentPrice();
        AssetModel assetToSell = assetService.findAssetByUserIdAndCoinId(user.getId(), coin.getId());

        if (assetToSell == null) {
            throw new Exception("Asset not found");
        }

        double buyPrice = assetToSell.getBuyPrice();

        if (assetToSell.getQuantity() < quantity) {
            throw new Exception("Insufficient quantity to sell");
        }

        OrderItem item = createOrderItem(coin, quantity, buyPrice, sellPrice);
        Order order = createOrder(user, item, OrderType.SELL);
        item.setOrder(order);
        order.setOrderStatus(OrderStatus.SUCCESS);

        Order savedOrder = orderRepository.save(order);

        walletService.payOrderPayment(order, user);

        AssetModel updatedAsset = assetService.updateAsset(assetToSell.getId(), -quantity);
        double updatedAssetValue = updatedAsset.getQuantity() * coin.getCurrentPrice();

        if (updatedAssetValue <= 1) {
            assetService.deleteAsset(updatedAsset.getId());
        }

        return savedOrder;
    }

    @Override
    @Transactional
    public Order processOrder(CoinMarketData coin, double quantity, OrderType orderType, User user) throws Exception {
        if (orderType.equals(OrderType.BUY)) {
            return buyAsset(coin, quantity, user);
        } else if (orderType.equals(OrderType.SELL)) {
            return sellAsset(coin, quantity, user);
        }
        throw new Exception("Invalid Order Type");
    }

    @Override
    public List<Order> getAllOrderOfUser(User user, OrderType orderType, String assetSymbol) {
        List<Order> orders = orderRepository.findByUserId(user.getId());

        return orders.stream()
                .filter(o -> orderType == null || o.getOrderType() == orderType)
                .filter(o -> assetSymbol == null || (o.getOrderItem() != null &&
                        assetSymbol.equalsIgnoreCase(o.getOrderItem().getCoin().getSymbol())))
                .collect(Collectors.toList());
    }
}
