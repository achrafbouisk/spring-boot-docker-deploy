package com.ecomteam.shop_dddv3.domain.repositories;

import com.ecomteam.shop_dddv3.domain.models.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findByProductId(String productId);


    @Query(value = "{}", fields = "{ 'id' : 1, 'rating' : 1, 'comment' : 1, 'user.id' : 1, 'user._id':0, 'user.username':0, 'user.email':0, 'user.password':0, 'user.roles':0, 'createdAt' : 1, 'updatedAt' : 1}")
    List<Review> findAllWithSpecifiedFields();


    // Optional<Review> createReview(String productId, Review review);

}