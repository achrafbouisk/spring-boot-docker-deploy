package com.ecomteam.shop_dddv3.controllers;

import com.ecomteam.shop_dddv3.domain.models.Category;
import com.ecomteam.shop_dddv3.domain.models.Product;
import com.ecomteam.shop_dddv3.domain.repositories.CategoryRepository;
import com.ecomteam.shop_dddv3.infrastructure.services.products.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/products")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    private final CategoryRepository categoryRepository;


    // GET ALL PRODUCT WITH SEARCH AND PEGINATION
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> getAllProductsBySearch(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(required = false) String keyword) {
        return productService.getAllProductsBySearch(pageNumber, keyword);
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        return productService.getProductById(id);
    }

//    @PostMapping
//    public ResponseEntity<String> createProduct(@RequestBody Product product ) {
//        return productService.createProduct(product);
//    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestParam("files") List<MultipartFile> files,
                                                @RequestParam("product") String productJson) {
        ObjectMapper objectMapper = new ObjectMapper();
        Product product;
        try {
            product = objectMapper.readValue(productJson, Product.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Error while parsing product data", HttpStatus.BAD_REQUEST);
        }

        return productService.createProduct(product, files);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProductById(@PathVariable String id, @RequestBody Product product) {
        return productService.updateProductById(id, product);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable String id) {
        return productService.deleteProductById(id);
    }
}

