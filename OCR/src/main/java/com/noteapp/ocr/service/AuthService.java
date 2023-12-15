package com.noteapp.ocr.service;
import com.noteapp.ocr.dto.AuthResultDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {
    @Value("${auth_url}")
    private String auth_url;

    public AuthResultDto authAccessToken(String access_token) {
        RestTemplate rt = new RestTemplate();
        String authUrlWithParams = auth_url + "/api/auth?access_token=" + access_token;
        return rt.getForObject(authUrlWithParams, AuthResultDto.class);
    }
}
