package com.noteapp.auth.service;
import com.noteapp.auth.entity.Token;
import com.noteapp.auth.repository.TokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }
    public boolean isTokenExpired(String accessToken) {
        LocalDateTime expiration = findExpirationByAccessToken(accessToken);
        LocalDateTime currentTime = LocalDateTime.now();
        if(expiration == null)
            return true;
        return !expiration.isAfter(currentTime);
    }

    @Transactional
    public void signUp(String userId, String accessToken, Integer expires_in) {
        LocalDateTime expiration = LocalDateTime.now().plusSeconds(expires_in - 100);
        Token token = new Token(userId, accessToken, expiration);
        tokenRepository.save(token);
    }
    @Transactional
    public void update(String userId, String accessToken, Integer expires_in) {
        LocalDateTime expiration = LocalDateTime.now().plusSeconds(expires_in - 100);
        Token token = tokenRepository.findById(userId).get();
        Token newToken = new Token(userId, accessToken, expiration);
        tokenRepository.save(newToken);
    }
    @Transactional
    public void deleteToken(String userId) {
        Token token = tokenRepository.findById(userId).get();
        tokenRepository.delete(token);
    }
    @Transactional(readOnly = true)
    public String findUserIdByAccessToken(String accessToken) {
        Token token = tokenRepository.findByAccessToken(accessToken);
        if(token == null) return null;
        return token.getUserId();
    }
    @Transactional(readOnly = true)
    public LocalDateTime findExpirationByAccessToken(String accessToken) {
        Token token = tokenRepository.findByAccessToken(accessToken);
        if(token == null)   return null;
        return token.getExpiration();
    }
}
