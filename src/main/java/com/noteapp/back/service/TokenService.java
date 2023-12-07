package com.noteapp.back.service;

import com.noteapp.back.entity.Token;
import com.noteapp.back.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    @Transactional
    public void signUp(String userId, String refresh_token, String access_token) {
        tokenRepository.save(new Token(userId, refresh_token, access_token));
    }

    @Transactional
    public void update(String userId, String access_token) {
        Token token = tokenRepository.findByUserId(userId);
        Token new_token = new Token(token.getUserId(), token.getRefreshToken(), access_token);
        tokenRepository.save(new_token);
    }

    @Transactional(readOnly = true)
    public String findRefreshTokenByUserId(String userId) {
        Token token = tokenRepository.findByUserId(userId);
        return token.getRefreshToken();
    }


}
