package com.noteapp.back.controller;

import com.noteapp.back.model.GoogleUserInfo;
import com.noteapp.back.dto.ResponseDto;
import com.noteapp.back.dto.UpdateDto;
import com.noteapp.back.service.GoogleOAuthService;
import com.noteapp.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;
import java.util.HashMap;

@RestController
public class DBController {
    @Autowired
    private UserService userService;
    @Autowired
    private GoogleOAuthService googleOAuthService;

    @GetMapping("/api/test")
    public String testGoogleAccessToken(@RequestParam String access_token) {
        try {
            GoogleUserInfo googleUserInfo = googleOAuthService.getGoogleUserInfo(access_token);
            return googleUserInfo.toString();
        } catch (HttpStatusCodeException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/api/database")
    public ResponseEntity<ResponseDto> updateDataBase(@RequestBody UpdateDto updateDto) {
        try {
            GoogleUserInfo googleUserInfo = googleOAuthService.getGoogleUserInfo(updateDto.getAccess_token());
            String userId = googleUserInfo.getId();
            if(!userService.existUserId(userId)) {
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", "User does not exist");
                }}));
            }
            userService.update(userId, updateDto.getData());
            return ResponseEntity.ok().body(new ResponseDto(true,
                    googleOAuthService.tokenRefreshing(userId),
                    new HashMap<String, Object>() {{
                put("message", "update success");
            }}));
        } catch (HttpStatusCodeException | IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                put("message", "Update failed: invalid access");
            }}));
        }
    }

    @GetMapping("/api/database")
    public ResponseEntity<ResponseDto> readDataBase(@RequestParam String access_token) {
        try {
            GoogleUserInfo googleUserInfo = googleOAuthService.getGoogleUserInfo(access_token);
            String userId = googleUserInfo.getId();
            if(!userService.existUserId(userId))
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", "User does not exist");
                }}));
            return ResponseEntity.ok().body(new ResponseDto(true,
                    googleOAuthService.tokenRefreshing(userId),
                    userService.readUserData(userId)));
        } catch (HttpStatusCodeException | IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                put("message", "read failed: invalid access");
            }}));
        }
    }

    @DeleteMapping("/api/database")
    public ResponseEntity<ResponseDto> deleteUser(@RequestParam String access_token) {
        try {
            GoogleUserInfo googleUserInfo = googleOAuthService.getGoogleUserInfo(access_token);
            String userId = googleUserInfo.getId();
            if(!userService.existUserId(userId))
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", "User does not exist");
                }}));
            userService.deleteUser(userId);
            return ResponseEntity.ok().body(new ResponseDto(true,
                    googleOAuthService.tokenRefreshing(userId),
                    new HashMap<String, Object>() {{
                put("message", "delete success");
            }}));
        } catch (HttpStatusCodeException | IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                put("message", "delete failed: invalid access");
            }}));
        }
    }
}
