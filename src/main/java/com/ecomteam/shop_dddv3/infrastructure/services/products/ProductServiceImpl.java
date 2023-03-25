package com.ecomteam.shop_dddv3.infrastructure.services.products;

import com.ecomteam.shop_dddv3.domain.models.Product;
import com.ecomteam.shop_dddv3.domain.repositories.ProductRepository;
import com.ecomteam.shop_dddv3.infrastructure.errors.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ErrorMessage error;

    // GET ALL PRODUCT WITH SEARCH AND PAGINATION
    @Override
    public ResponseEntity<Map<String, Object>> getAllProductsBySearch(int pageNumber, String keyword) {
        int pageSize = 6;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("id").descending());
        Page<Product> productPage;
        if (keyword != null) {
            productPage = productRepository.findByNameContainingIgnoreCase(keyword, pageable);
        } else {
            productPage = productRepository.findAll(pageable);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("products", productPage.getContent());
        response.put("page", productPage.getNumber() + 1);
        response.put("pages", productPage.getTotalPages());
        return ResponseEntity.ok(response);
    }

    // GET ALL PRODUCT
    @Override
    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productRepository.findAll();
        if (!products.isEmpty()) {
            return new ResponseEntity<>(products, HttpStatus.OK);
        } else {
            error.setMessage("Products not found");
            return new ResponseEntity<>(error.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    // CREATE PRODUCT
//    @Override
//    public ResponseEntity<String> createProduct(Product product) {
//
//        Product saveProd = productRepository.save(product);
//        if (saveProd != null)
//            return new ResponseEntity<>("Product saved successfully!", HttpStatus.CREATED);
//        else{
//            error.setMessage("Error while saving product");
//            return new ResponseEntity<>(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @Override
    public ResponseEntity<String> createProduct(Product product, List<MultipartFile> files) {

        // Store image names in the product object
        List<String> imageNames = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            imageNames.add(fileName);
            try {
                // Store the image file locally
                Path path = Paths.get("./uploads/" + fileName);
                Files.write(path, file.getBytes());
            } catch (IOException e) {
                error.setMessage("Error while saving image: " + fileName);
                return new ResponseEntity<>(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        product.setImages(imageNames);

        Product saveProd = productRepository.save(product);
        if (saveProd != null)
            return new ResponseEntity<>("Product saved successfully!", HttpStatus.CREATED);
        else{
            error.setMessage("Error while saving product");
            return new ResponseEntity<>(error.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET SINGLE PRODUCT
    @Override
    public ResponseEntity<?> getProductById(String id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null)
            return new ResponseEntity<>(product, HttpStatus.OK);
        else{
            error.setMessage("Product not found");
            return new ResponseEntity<>(error.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    // DELETE PRODUCT
    @Override
    public ResponseEntity<String> deleteProductById(String id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return new ResponseEntity<>("Product deleted successfully!", HttpStatus.OK);
        } else {
            error.setMessage("Product not found");
            return new ResponseEntity<>(error.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // UPDATE PRODUCT
    @Override
    public ResponseEntity<String> updateProductById(String id, Product product) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product productReq = existingProduct.get();
            productReq.setName(product.getName());
            productReq.setDescription(product.getDescription());
            productReq.setPrice(product.getPrice());
            productReq.setStock(product.getStock());
            productReq.setImages(product.getImages());
            productRepository.save(productReq);
            return new ResponseEntity<>("Product has been updated successfully", HttpStatus.ACCEPTED);
        } else {
            error.setMessage("Error while updating product");
            return new ResponseEntity<>(error.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
