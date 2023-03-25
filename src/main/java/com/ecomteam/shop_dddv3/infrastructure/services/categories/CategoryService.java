package com.ecomteam.shop_dddv3.infrastructure.services.categories;

import com.ecomteam.shop_dddv3.domain.models.Category;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<?> getAllCategories();

    ResponseEntity<String> createCategory(Category category);

    ResponseEntity<String> updateCategory(String id, Category category);

    ResponseEntity<String> deleteCategory(String id);

}
