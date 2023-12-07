package com.noteapp.back.controller;

import com.noteapp.back.dto.ResponseDto;
import com.noteapp.back.service.GoogleOAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;
import java.util.HashMap;

@RestController
public class LoginController {
    private final GoogleOAuthService googleOAuthService;

    @Autowired
    public LoginController(GoogleOAuthService googleOAuthService) {
        this.googleOAuthService = googleOAuthService;
    }

    @GetMapping("/login")
    public ResponseEntity<ResponseDto> googleLogin(@RequestParam("code") String code) throws IOException {
        try {
            //System.out.println("구글 인가코드: " + code);
            return googleOAuthService.googleLogin(code);
        } catch (HttpStatusCodeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                put("message", "Login failed: invalid code");
            }}));
        }
    }

    @GetMapping("/login/test")
    public String test() {
        return "test";
    }
}
