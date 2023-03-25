package com.ecomteam.shop_dddv3.infrastructure.exceptions;

public class ProductAlreadyReviewedException extends Exception {
    public ProductAlreadyReviewedException(String message) {
        super(message);
    }
}