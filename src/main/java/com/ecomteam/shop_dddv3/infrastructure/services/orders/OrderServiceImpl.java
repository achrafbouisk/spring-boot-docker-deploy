package com.ecomteam.shop_dddv3.infrastructure.services.orders;

import com.ecomteam.shop_dddv3.domain.models.Order;
import com.ecomteam.shop_dddv3.domain.models.Product;
import com.ecomteam.shop_dddv3.domain.models.User;
import com.ecomteam.shop_dddv3.domain.repositories.OrderRepository;
import com.ecomteam.shop_dddv3.domain.repositories.ProductRepository;
import com.ecomteam.shop_dddv3.domain.repositories.UserRepository;
import com.ecomteam.shop_dddv3.infrastructure.errors.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final MongoTemplate  mongoTemplate;
    private final ErrorMessage errorMessage;

    @Override
    public ResponseEntity<String> createOrder(Order order) {
        Order newOrder = orderRepository.save(order);
        if(newOrder != null){
            return new ResponseEntity<>("Order has been saved successfully", HttpStatus.CREATED);
        }
        else {
            errorMessage.setMessage("Error while saving order");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> getOrderById(String id) {
        Order order = orderRepository.findById(id).orElse(null);
        if(order != null){
            Order orderList = getJustUserId(order);
            return new ResponseEntity<>(orderList, HttpStatus.OK);
        }
        else {
            errorMessage.setMessage("Order Not Found");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> getAllOrders() {
        List<Order> order =  mongoTemplate.findAll(Order.class);
        if(!order.isEmpty()){
            List<Order> orders = ListGetJustUserId(order);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        else {
            errorMessage.setMessage("Orders Not Found");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> updateOrder(String id, Order order) {
        Order existingOrder = orderRepository.findById(id).orElse(null);
        if (existingOrder == null) {
            errorMessage.setMessage("Order Not Found");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.NOT_FOUND);
        }
        existingOrder.setUser(order.getUser());
        existingOrder.setItems(order.getItems());
        existingOrder.setShippingAddress(order.getShippingAddress());
        existingOrder.setShippingCost(order.getShippingCost());
        existingOrder.setPaymentMethod(order.getPaymentMethod());
        existingOrder.setTotalPrice(order.getTotalPrice());
        existingOrder.setPaid(false);
        existingOrder.setPaidAt(LocalDateTime.now());
        existingOrder.setDelivered(false);
        existingOrder.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(existingOrder);
        return new ResponseEntity<>("Order has been updated successfully", HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<String> deleteOrder(String id) {
        Order existingOrder = orderRepository.findById(id).orElse(null);
        if(existingOrder != null) {
            orderRepository.deleteById(id);
            return new ResponseEntity<>("Order is deleted successfully",HttpStatus.OK);
        }
        else {
            errorMessage.setMessage("Order Not Found");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public ResponseEntity<?> getOrdersByUser(Authentication authentication) {
        String userId = ((User) authentication.getPrincipal()).getId();
        List<Order> order = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        if(!order.isEmpty())
            return new ResponseEntity<>(order,HttpStatus.OK);
        else {
            errorMessage.setMessage("Order Not Found");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> orderIsPaid(String id) {
        Order existingOrder = orderRepository.findById(id).orElse(null);
        if (existingOrder == null) {
            errorMessage.setMessage("Order Not Found");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.NOT_FOUND);
        }
        existingOrder.setPaid(true);
        existingOrder.setPaidAt(LocalDateTime.now());
        existingOrder.getItems().forEach(orderItem -> updateStock(orderItem.getProduct().getId(), orderItem.getQuantity()));
        orderRepository.save(existingOrder);
        return new ResponseEntity<>("Order payed successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> orderIsDelivered(String id) {
        Order existingOrder = orderRepository.findById(id).orElse(null);
        if (existingOrder == null) {
            errorMessage.setMessage("Order Not Found");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.NOT_FOUND);
        }
        existingOrder.setDelivered(true);
        existingOrder.setDeliveredAt(LocalDateTime.now());
        orderRepository.save(existingOrder);
        return new ResponseEntity<>("Order delivered successfully", HttpStatus.OK);
    }

    //function get just id in user with order
    private Order getJustUserId(Order order){
            //user
            String userIdFromOrder = order.getUser().getId();
            User user = userRepository.findById(userIdFromOrder).orElse(null);
            if (user != null) {
                User userWithIdOnly = new User();
                userWithIdOnly.setId(user.getId());
                userWithIdOnly.setUsername(user.getUsername());
                order.setUser(userWithIdOnly);
            }
            else order.setUser(null);

        return order;
    }

    private List<Order> ListGetJustUserId(List<Order> orders){
        for(Order order:orders){
            //user
            String userIdFromOrder = order.getUser().getId();
            User user = userRepository.findById(userIdFromOrder).orElse(null);
            if (user != null) {
                User userWithIdOnly = new User();
                userWithIdOnly.setId(userIdFromOrder);
                userWithIdOnly.setUsername(user.getUsername());
                order.setUser(userWithIdOnly);
            }
            else order.setUser(null);
        }
        return orders;
    }

    //Update stock
    public void updateStock(String id, int quantity) {
        Product product = productRepository.findById(id).orElse(null);

        product.setStock(product.getStock() - quantity);

        productRepository.save(product);
    }
}
