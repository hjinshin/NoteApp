package com.noteapp.back.controller;

import com.noteapp.back.dto.ResponseDto;
import com.noteapp.back.dto.SignUpDto;
import com.noteapp.back.dto.UpdateDto;
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

    @PostMapping("/api/signup")
    public ResponseEntity<ResponseDto> signUp(@RequestBody SignUpDto signUpDto) {
        try{
            if(userService.existUserId(signUpDto.getUserId()))
                return ResponseEntity.ok().body(new ResponseDto(false, new HashMap<String, Object>() {{
                    put("message", "UserId already exists");
                }}));
            userService.signUp(signUpDto.getUserId());
            return ResponseEntity.ok().body(new ResponseDto(true, new HashMap<String, Object>() {{
                put("message", "SignUp success");
            }}));
        } catch (HttpStatusCodeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(false, new HashMap<String, Object>() {{
                put("message", "SignUp failed");
            }}));
        }

    }

    @PostMapping("/api/database")
    public ResponseEntity<ResponseDto> updateDataBase(@RequestBody UpdateDto updateDto) {
        try {
            if(!userService.existUserId(updateDto.getUserId()))
                return ResponseEntity.ok().body(new ResponseDto(false, new HashMap<String, Object>() {{
                    put("message", "UserId does not exist");
                }}));
            userService.update(updateDto.getUserId(), updateDto.getData());
            return ResponseEntity.ok().body(new ResponseDto(true, new HashMap<String, Object>() {{
                put("message", "update success");
            }}));
        } catch (HttpStatusCodeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(false, new HashMap<String, Object>() {{
                put("message", "Update failed");
            }}));
        }
    }

    @GetMapping("/api/database")
    public ResponseEntity<ResponseDto> readDataBase(@RequestParam String userId) {
        try {
            if(!userService.existUserId(userId))
                return ResponseEntity.ok().body(new ResponseDto(false, new HashMap<String, Object>() {{
                    put("message", "UserId does not exist");
                }}));
            return ResponseEntity.ok().body(new ResponseDto(true, userService.readUserData(userId)));
        } catch (HttpStatusCodeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(false, new HashMap<String, Object>() {{
                put("message", "read failed");
            }}));
        }
    }

    @DeleteMapping("/api/database")
    public ResponseEntity<ResponseDto> deleteUser(@RequestParam String userId) {
        try {
            if(!userService.existUserId(userId))
                return ResponseEntity.ok().body(new ResponseDto(false, new HashMap<String, Object>() {{
                    put("message", "UserId does not exist");
                }}));
            userService.deleteUser(userId);
            return ResponseEntity.ok().body(new ResponseDto(true, new HashMap<String, Object>() {{
                put("message", "delete success");
            }}));
        } catch (HttpStatusCodeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(false, new HashMap<String, Object>() {{
                put("message", "delete failed");
            }}));
        }
    }
}
