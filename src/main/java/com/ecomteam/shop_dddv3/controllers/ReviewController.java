package com.ecomteam.shop_dddv3.controllers;

import com.ecomteam.shop_dddv3.domain.models.Review;
import com.ecomteam.shop_dddv3.infrastructure.services.reviews.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController 
@RequestMapping("/api/v2/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // GET ALL REVIEWS
    @GetMapping
    public ResponseEntity<?> getAllReviews() {
        return reviewService.getAllReviews();
    }

    // GET REVIEWS BY PRODUCT ID
    @GetMapping("/products/{productId}")
    public ResponseEntity<?> getReviewsByProductId(@PathVariable String productId) {
        return reviewService.getReviewsByProductId(productId);
    }

    @PostMapping("/products/{productId}")
    public ResponseEntity<String> createReview(@PathVariable String productId, @RequestBody Review review) {
        return reviewService.createReview(productId, review);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable String id) {
        return reviewService.deleteReview(id);
    }

}
