package com.ecomteam.shop_dddv3.domain.repositories;

import com.ecomteam.shop_dddv3.domain.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String>{
    List<Order> findByUserIdOrderByCreatedAtDesc(String userId);
}