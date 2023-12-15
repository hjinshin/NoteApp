package com.noteapp.auth.repository;

import com.noteapp.auth.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, String> {
    Token findByAccessToken(String accessToken);
}
