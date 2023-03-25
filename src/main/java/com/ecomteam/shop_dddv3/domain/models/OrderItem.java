package com.ecomteam.shop_dddv3.domain.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("order_items")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItem {

    private int quantity;

    private double price;

    @DBRef
    private Product product;
}
