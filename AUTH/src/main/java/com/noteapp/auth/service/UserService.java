package com.noteapp.auth.service;

import com.noteapp.auth.entity.User;
import com.noteapp.auth.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Transactional
    public void signUp(String userId, String email, String refreshToken) {
        User user = new User(userId, email, refreshToken);
        userRepository.save(user);
    }
    @Transactional
    public void updateRefreshToken(String userId, String refreshToken) {
        User user = userRepository.findById(userId).get();
        User newUser = new User(userId, user.getEmail(), refreshToken);
        userRepository.save(newUser);
    }
    @Transactional(readOnly = true)
    public String findRefreshTokenByUserId(String userId) {
        User user = userRepository.findById(userId).get();
        return user.getRefreshToken();
    }
    @Transactional(readOnly = true)
    public boolean existUserId(String userId) {
        return userRepository.existsById(userId);
    }
}
