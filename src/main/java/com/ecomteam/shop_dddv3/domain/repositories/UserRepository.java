package com.ecomteam.shop_dddv3.domain.repositories;

import com.ecomteam.shop_dddv3.domain.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
  
    Boolean existsByUsername(String username);
  
    Boolean existsByEmail(String email);

    User findByEmailIgnoreCase(String email);

    Optional<User> findByEmail(String email);


}