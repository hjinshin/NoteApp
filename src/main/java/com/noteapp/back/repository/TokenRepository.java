package com.noteapp.back.repository;

import com.noteapp.back.entity.Token;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<Token, String> {
    Token findByUserId(String userId);
}
