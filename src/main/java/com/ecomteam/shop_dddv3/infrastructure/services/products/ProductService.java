package com.ecomteam.shop_dddv3.infrastructure.services.products;

import com.ecomteam.shop_dddv3.domain.models.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ProductService {
    ResponseEntity<?> getAllProducts();

    ResponseEntity<String> createProduct(Product product, List<MultipartFile> files);


    ResponseEntity<?> getProductById(String id);

    ResponseEntity<String> deleteProductById(String id);

    ResponseEntity<String> updateProductById(String id, Product product);

    ResponseEntity<Map<String, Object>> getAllProductsBySearch(int pageNumber, String keyword);
}
