package com.ecomteam.shop_dddv3.domain.repositories;

import com.ecomteam.shop_dddv3.domain.models.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends MongoRepository<Token, String> {

    @Query("{'user.id': ?0, $or: [{'expired': false}, {'revoked': false}]}")
    List<Token> findAllValidTokenByUser(String id);

    Optional<Token> findByToken(String token);
}
