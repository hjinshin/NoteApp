package com.noteapp.back.service;

import com.noteapp.back.dto.PageUpdateDto;
import com.noteapp.back.dto.UpdateAllDto;
import com.noteapp.back.entity.User;
import com.noteapp.back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void signUp(String userId, String userEmail) {
        User user = new User(userId, userEmail, new ArrayList<>());
        userRepository.save(user);
    }
    @Transactional
    public void updateAll(String userId, List<UpdateAllDto.NewPage> newPages) {
        User user = userRepository.findByUserId(userId);
    }
    @Transactional
    public void pageUpdate(String userId, String oldPageName, PageUpdateDto.NewPage newPageDto) {
        User user = userRepository.findByUserId(userId);
        for(User.Page page: user.getPages()) {
            if(page.getName().equals(oldPageName)) {
                page.setName(newPageDto.getName());
                page.setData(newPageDto.getData());
                page.setLast_modified_date(LocalDateTime.now());
                userRepository.save(user);
                return;
            }
        }
        User.Page newPage = new User.Page(newPageDto.getName(), newPageDto.getData(), LocalDateTime.now());
        Objects.requireNonNull(user).getPages().add(newPage);

        userRepository.save(user);
    }
    @Transactional
    public void deleteUser(String userId) {
        User user = userRepository.findByUserId(userId);
        userRepository.delete(user);
    }
    @Transactional
    public void deletePageByName(String userId, String pageName) {
        User user = userRepository.findByUserId(userId);
        if(user != null && user.getPages() != null) {
            user.getPages().removeIf(page->page.getName().equals(pageName));
            userRepository.save(user);
        }
    }
    public List<String> getAllPageNamesByUserId(String userId) {
        User user = userRepository.findByUserId(userId);

        if(user != null && user.getPages() != null) {
            return user.getPages().stream()
                    .map(User.Page::getName)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public boolean existPageName(String userId, String pageName) {
        User user = userRepository.findByUserId(userId);
        for(User.Page page: user.getPages()) {
            if(page.getName().equals(pageName)) {
                return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    public List<User.Page> readUserData(String userId) {
        User user = userRepository.findByUserId(userId);
        return user.getPages();
    }
    @Transactional(readOnly = true)
    public User.Page readPage(String userId, String pageName) {
        User user = userRepository.findByUserId(userId);
        if(user != null && user.getPages() != null) {
            for(User.Page page: user.getPages()) {
                if(page.getName().equals(pageName)) {
                    return page;
                }
            }
        }
        return new User.Page();
    }

    @Transactional(readOnly = true)
    public boolean existUserId(String userId) {
        return userRepository.existsByUserId(userId);
    }
}
