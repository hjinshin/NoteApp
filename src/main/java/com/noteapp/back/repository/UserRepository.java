package com.noteapp.back.repository;

import com.noteapp.back.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

// DAO
public interface UserRepository extends MongoRepository<User, String> {
    User findByUserId(String userId);
    boolean existsByUserId(String userId);

}
