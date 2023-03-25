package com.ecomteam.shop_dddv3.infrastructure.services.categories;

import com.ecomteam.shop_dddv3.domain.models.Category;
import com.ecomteam.shop_dddv3.domain.repositories.CategoryRepository;
import com.ecomteam.shop_dddv3.infrastructure.errors.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;
    private final ErrorMessage errorMessage;

    @Override
    public ResponseEntity<?> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if(!categories.isEmpty())
            return new ResponseEntity<>(categories,HttpStatus.OK);
        else {
            errorMessage.setMessage("Categories Not Found");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<String> createCategory(Category category) {
        Category category1 = categoryRepository.save(category);
        if(category1 != null)
            return new ResponseEntity<>("Category saved successfully", HttpStatus.CREATED);
        else {
            errorMessage.setMessage("Error while saving category");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> updateCategory(String id, Category category) {
        Optional<Category> exist = categoryRepository.findById(id);
        if(exist.isPresent()){
            Category req = exist.get();
            req.setName(category.getName());
            req.setDescription(category.getDescription());
            req.setSubcategories(category.getSubcategories());
            categoryRepository.save(req);
            return new ResponseEntity<>("Category has been updated successfully",HttpStatus.ACCEPTED);
        }
        else {
            errorMessage.setMessage("Error while updating category");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> deleteCategory(String id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return new ResponseEntity<>("Category deleted successfully!", HttpStatus.OK);
        } else {
            errorMessage.setMessage("Error while deleting category");
            return new ResponseEntity<>(errorMessage.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

