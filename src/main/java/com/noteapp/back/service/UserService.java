package com.noteapp.back.service;

import com.noteapp.back.entity.User;
import com.noteapp.back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void signUp(String userId, String userEmail) {
        User user = new User(userId, userEmail, new HashMap<>(), LocalDateTime.now());
        userRepository.save(user);
    }

    @Transactional
    public void update(String userId, Map<String ,Object> data) {
        User user = userRepository.findByUserId(userId);
        User new_user = new User(userId, user.getEmail(), data, LocalDateTime.now());
        userRepository.save(new_user);
    }

    @Transactional
    public void deleteUser(String userId) {
        User user = userRepository.findByUserId(userId);
        userRepository.delete(user);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> readUserData(String userId) {
        User user = userRepository.findByUserId(userId);
        return user.getData();
    }

    @Transactional(readOnly = true)
    public boolean existUserId(String userId) {
        return userRepository.existsByUserId(userId);
    }
}
