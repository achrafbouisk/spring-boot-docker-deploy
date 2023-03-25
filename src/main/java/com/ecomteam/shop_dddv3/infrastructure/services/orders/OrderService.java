package com.ecomteam.shop_dddv3.infrastructure.services.orders;

import com.ecomteam.shop_dddv3.domain.models.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface OrderService {
    
    ResponseEntity<String> createOrder(Order order);

    ResponseEntity<?> getOrderById(String id);

    ResponseEntity<?> getAllOrders();

    ResponseEntity<String> updateOrder(String id, Order order);

    ResponseEntity<String> deleteOrder(String id);

    ResponseEntity<?> getOrdersByUser(Authentication authentication);

    ResponseEntity<?> orderIsPaid(String id);

    ResponseEntity<?> orderIsDelivered(String id);



}
