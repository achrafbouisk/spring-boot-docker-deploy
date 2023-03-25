package com.ecomteam.shop_dddv3.infrastructure.exceptions;

public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
