package com.ecomteam.shop_dddv3.infrastructure.services.reviews;

import com.ecomteam.shop_dddv3.domain.models.Review;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.http.ResponseEntity;

public interface ReviewService {

    ResponseEntity<?> getAllReviews();

    ResponseEntity<String> createReview(String productId, Review review);

    ResponseEntity<String> deleteReview(String id);

    @Query("{'product' :{'$ref' : 'product' , '$id' : ?0}}")
    ResponseEntity<?> getReviewsByProductId(String productId);
}
