package com.ecomteam.shop_dddv3.infrastructure.services.reviews;

import com.ecomteam.shop_dddv3.domain.models.Product;
import com.ecomteam.shop_dddv3.domain.models.Review;
import com.ecomteam.shop_dddv3.domain.models.User;
import com.ecomteam.shop_dddv3.domain.repositories.ProductRepository;
import com.ecomteam.shop_dddv3.domain.repositories.ReviewRepository;
import com.ecomteam.shop_dddv3.domain.repositories.UserRepository;
import com.ecomteam.shop_dddv3.infrastructure.errors.ErrorMessage;
import com.ecomteam.shop_dddv3.infrastructure.exceptions.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;
    private final ErrorMessage errorMessage;

    // GET ALL REVIEWS
    @Override
    public ResponseEntity<?> getAllReviews() {

        List<Review> reviews = mongoTemplate.findAll(Review.class);
        if (!reviews.isEmpty()) {
            List<Review> reviewList = productAndUserId(reviews);
            return new ResponseEntity<>(reviewList, HttpStatus.OK);
        } else {
            errorMessage.setMessage("Reviews Not found");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.NOT_FOUND);
        }

        // Query method

        /*List<Review> reviews = reviewRepository.findAllWithSpecifiedFields();
         if (!reviews.isEmpty()) {
             return new ResponseEntity<>(reviews, HttpStatus.OK);
         } else {
             errorMessage.setMessage("Reviews Not found");
             return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.NOT_FOUND);
         }*/

    }

    // GET REVIEWS BY PRODUCT ID
    @Override
    public ResponseEntity<?> getReviewsByProductId(String productId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("product.id").is(productId));
        List<Review> reviews = mongoTemplate.find(query, Review.class);
        if (!reviews.isEmpty()) {
            List<Review> reviewList = productAndUserId(reviews);
            return new ResponseEntity<>(reviewList, HttpStatus.OK);
        } else {
            errorMessage.setMessage("Reviews Not Found");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // CREATE REVIEW
    @Override
    public ResponseEntity<String> createReview(String productId, Review review) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        review.setProduct(product);
        Review savedReview = reviewRepository.save(review);
        if (savedReview != null)
            return new ResponseEntity<>("Review saved successfully!", HttpStatus.CREATED);
        else {
            errorMessage.setMessage("Error while saving review");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE PRODUCT
    @Override
    public ResponseEntity<String> deleteReview(String id) {
        if (reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
            return new ResponseEntity<>("Review deleted successfully!", HttpStatus.OK);
        } else {
            errorMessage.setMessage("Review Not Found");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // function about get just id for product and user in review
    private List<Review> productAndUserId(List<Review> reviews) {
        for (Review review : reviews) {
            // product
            String productIdFromReview = review.getProduct().getId();
            Product product1 = productRepository.findById(productIdFromReview).orElse(null);
            if (product1 != null) {
                Product product = new Product();
                product.setId(productIdFromReview);
                review.setProduct(product);
            } else
                review.setProduct(null);
            // user
            String userIdFromReview = review.getUser().getId();
            User user = userRepository.findById(userIdFromReview).orElse(null);
            if (user != null) {
                User userWithIdOnly = new User();
                userWithIdOnly.setId(userIdFromReview);
                userWithIdOnly.setUsername(user.getUsername());
                review.setUser(userWithIdOnly);
            } else
                review.setUser(null);
        }
        return reviews;
    }
}