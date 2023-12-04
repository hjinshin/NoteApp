package com.noteapp.back.service;

import com.noteapp.back.dto.GoogleUserInfoDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;

@Service
public class GoogleOAuthService {
    public GoogleUserInfoDto getGoogleUserInfo(String access_token) {
        ResponseEntity<Map<String, Object>> userInfoResponse = requestGoogleUserInfo(access_token);
        return getGoogleUserInfo(userInfoResponse);
    }

    public ResponseEntity<Map<String, Object>> requestGoogleUserInfo(String access_token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        return rt.exchange(
                "https://www.googleapis.com/oauth2/v3/userinfo",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    public GoogleUserInfoDto getGoogleUserInfo(ResponseEntity<Map<String, Object>> response) {
        Map<String, Object> userInfoMap = response.getBody();
        return new GoogleUserInfoDto(Objects.requireNonNull(userInfoMap).get("sub").toString(), userInfoMap.get("email").toString());
    }
}
