package com.noteapp.auth.controller;

import com.noteapp.auth.dto.AuthResultDto;
import com.noteapp.auth.model.GoogleUserInfo;
import com.noteapp.auth.service.GoogleOAuthService;
import com.noteapp.auth.service.TokenService;
import com.noteapp.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;

@RestController
public class AuthController {
    private final GoogleOAuthService googleOAuthService;
    private final UserService userService;
    private final TokenService tokenService;
    @Autowired
    public AuthController(GoogleOAuthService googleOAuthService, UserService userService, TokenService tokenService) {
        this.googleOAuthService = googleOAuthService;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @GetMapping("/api/auth")
    public AuthResultDto authenticateAccessToken(@RequestParam String access_token) {
        try {
            String accessToken = access_token;
            if(tokenService.isTokenExpired(accessToken)) {
                accessToken = googleOAuthService.refreshAccessToken(accessToken);
                if(accessToken == null)
                    return (new AuthResultDto(false, null, null, "User does not exist"));
            }
            GoogleUserInfo googleUserInfo = googleOAuthService.getGoogleUserInfo(accessToken);
            if(!userService.existUserId(googleUserInfo.getId())) {
                return (new AuthResultDto(false, null, null, "User does not exist"));
            }
            return (new AuthResultDto(true, accessToken, googleUserInfo.getId(), "Success"));
        } catch (HttpStatusCodeException | IOException e) {
            System.out.println(e.getMessage());
            return (new AuthResultDto(false, null, null, "Authentication failed"));
        }
    }
    @GetMapping("/api/revoke")
    public ResponseEntity<String> revokeAccessToken(@RequestParam String access_token) {
        return googleOAuthService.requestAccessToken(access_token);
    }
}
