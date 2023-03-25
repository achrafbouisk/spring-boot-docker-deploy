package com.ecomteam.shop_dddv3.controllers;

import com.ecomteam.shop_dddv3.domain.models.Order;
import com.ecomteam.shop_dddv3.infrastructure.services.orders.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/orders")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderController {
    
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id) {
        return orderService.getOrderById(id);
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateOrder(@PathVariable String id, @RequestBody Order order) {
        return orderService.updateOrder(id, order);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable String id) {
        return orderService.deleteOrder(id);
    }

    //GET ORDERS BY USER
    @GetMapping("/byUser")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOrdersByUser(Authentication authentication) {
        return orderService.getOrdersByUser(authentication);
    }

    // ORDER IS PAID
    @PutMapping("/{id}/payed")
    public ResponseEntity<?> updateOrderToPaid(@PathVariable String id) {
        return orderService.orderIsPaid(id);
    }


    // ORDER IS DELIVERED
    @PutMapping("/{id}/delivered")
    public ResponseEntity<?> updateOrderToDelivered(@PathVariable String id) {
        return orderService.orderIsDelivered(id);
    }
}
