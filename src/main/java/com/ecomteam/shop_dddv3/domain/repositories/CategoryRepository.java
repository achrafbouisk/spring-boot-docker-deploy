package com.ecomteam.shop_dddv3.domain.repositories;

import com.ecomteam.shop_dddv3.domain.models.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
}