package com.noteapp.back.controller;

import com.noteapp.back.dto.GoogleUserInfoDto;
import com.noteapp.back.dto.ResponseDto;
import com.noteapp.back.dto.SignUpDto;
import com.noteapp.back.dto.UpdateDto;
import com.noteapp.back.service.GoogleOAuthService;
import com.noteapp.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

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
            GoogleUserInfoDto googleUserInfoDto = googleOAuthService.getGoogleUserInfo(access_token);
            return googleUserInfoDto.toString();
        } catch (HttpStatusCodeException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/api/signup")
    public ResponseEntity<ResponseDto> signUp(@RequestBody SignUpDto signUpDto) {
        try{
            GoogleUserInfoDto googleUserInfoDto = googleOAuthService.getGoogleUserInfo(signUpDto.getAccess_token());
            String userId = googleUserInfoDto.getId();
            if(userService.existUserId(userId))
                return ResponseEntity.ok().body(new ResponseDto(false, new HashMap<String, Object>() {{
                    put("message", "User already exists");
                }}));
            userService.signUp(userId, googleUserInfoDto.getEmail());
            return ResponseEntity.ok().body(new ResponseDto(true, new HashMap<String, Object>() {{
                put("message", "SignUp success");
            }}));
        } catch (HttpStatusCodeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(false, new HashMap<String, Object>() {{
                put("message", "SignUp failed: invalid access_token");
            }}));
        }

    }

    @PostMapping("/api/database")
    public ResponseEntity<ResponseDto> updateDataBase(@RequestBody UpdateDto updateDto) {
        try {
            GoogleUserInfoDto googleUserInfoDto = googleOAuthService.getGoogleUserInfo(updateDto.getAccess_token());
            String userId = googleUserInfoDto.getId();
            System.out.println(userId);
            if(!userService.existUserId(userId))
                return ResponseEntity.ok().body(new ResponseDto(false, new HashMap<String, Object>() {{
                    put("message", "User does not exist");
                }}));
            userService.update(userId, updateDto.getData());
            return ResponseEntity.ok().body(new ResponseDto(true, new HashMap<String, Object>() {{
                put("message", "update success");
            }}));
        } catch (HttpStatusCodeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(false, new HashMap<String, Object>() {{
                put("message", "Update failed: invalid access");
            }}));
        }
    }

    @GetMapping("/api/database")
    public ResponseEntity<ResponseDto> readDataBase(@RequestParam String access_token) {
        try {
            GoogleUserInfoDto googleUserInfoDto = googleOAuthService.getGoogleUserInfo(access_token);
            String userId = googleUserInfoDto.getId();
            if(!userService.existUserId(userId))
                return ResponseEntity.ok().body(new ResponseDto(false, new HashMap<String, Object>() {{
                    put("message", "User does not exist");
                }}));
            return ResponseEntity.ok().body(new ResponseDto(true, userService.readUserData(userId)));
        } catch (HttpStatusCodeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(false, new HashMap<String, Object>() {{
                put("message", "read failed: invalid access");
            }}));
        }
    }

    @DeleteMapping("/api/database")
    public ResponseEntity<ResponseDto> deleteUser(@RequestParam String access_token) {
        try {
            GoogleUserInfoDto googleUserInfoDto = googleOAuthService.getGoogleUserInfo(access_token);
            String userId = googleUserInfoDto.getId();
            if(!userService.existUserId(userId))
                return ResponseEntity.ok().body(new ResponseDto(false, new HashMap<String, Object>() {{
                    put("message", "User does not exist");
                }}));
            userService.deleteUser(userId);
            return ResponseEntity.ok().body(new ResponseDto(true, new HashMap<String, Object>() {{
                put("message", "delete success");
            }}));
        } catch (HttpStatusCodeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(false, new HashMap<String, Object>() {{
                put("message", "delete failed: invalid access");
            }}));
        }
    }
}
