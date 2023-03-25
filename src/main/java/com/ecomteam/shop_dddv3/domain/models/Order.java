package com.ecomteam.shop_dddv3.domain.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "orders")
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    private String id;

    @DBRef
    private User user;

    private List<OrderItem> items;

    private Address shippingAddress;

    private Address billingAddress; //payment address

    private BigDecimal shippingCost;

    private String paymentMethod;

    private BigDecimal totalPrice;

    private boolean paid;

    private LocalDateTime paidAt;

    private boolean delivered;

    private LocalDateTime deliveredAt;


    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

}