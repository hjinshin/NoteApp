package com.noteapp.back.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noteapp.back.dto.GoogleOAuthTokenDto;
import com.noteapp.back.model.GoogleToken;
import com.noteapp.back.model.GoogleUserInfo;
import com.noteapp.back.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleOAuthService {
    @Value("${google_clientId}")
    private String google_clientId;
    @Value("${google_secret}")
    private String google_secret;
    private final UserService userService;
    private final TokenService tokenService;

    private final String GOOGLE_TOKEN_REQUEST_URL = "https://oauth2.googleapis.com/token";
    private final String GOOGLE_USERINFO_REQUEST_URL = "https://www.googleapis.com/oauth2/v3/userinfo";
    @Autowired
    public GoogleOAuthService(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    public ResponseEntity<ResponseDto> googleLogin(String code) throws IOException {
        // access_token 인증
        ResponseEntity<String> accessTokenResponse = requestAccessToken(code);
        if(accessTokenResponse.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                put("message", "Login failed: invalid code");
            }}));
        }
        GoogleOAuthTokenDto oAuthTokenDto = getAccessToken(accessTokenResponse);
        // acccess_token으로 userInfo 가져오기
        GoogleUserInfo googleUserInfo = getGoogleUserInfo(oAuthTokenDto.getAccess_token());
        // User 및 Token document 생성
        String userId = googleUserInfo.getId();
        if(!userService.existUserId(userId))
            userService.signUp(userId, googleUserInfo.getEmail());
        if(oAuthTokenDto.getRefresh_token()!=null) {
            GoogleToken refreshTokenDto = new GoogleToken(googleUserInfo.getId(), oAuthTokenDto.getRefresh_token(), oAuthTokenDto.getAccess_token());
            tokenService.signUp(refreshTokenDto.getId(), refreshTokenDto.getRefresh_token(), refreshTokenDto.getAccess_token());
        }

        return ResponseEntity.ok().body(new ResponseDto(true,
                tokenRefreshing(userId),
                new HashMap<String, Object>() {{
                    put("nameList", userService.getAllPageNamesByUserId(userId));
        }}));
    }

    public String tokenRefreshing(String userId) throws IOException {
        String refresh_token = tokenService.findRefreshTokenByUserId(userId);
        ResponseEntity<String> accessTokenResponse = requestRefreshedAccessToken(refresh_token);
        GoogleOAuthTokenDto oAuthTokenDto = getAccessToken(accessTokenResponse);
        tokenService.update(userId, oAuthTokenDto.getAccess_token());
        return oAuthTokenDto.getAccess_token();
    }

    public ResponseEntity<String> requestAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", google_clientId);
        params.add("client_secret", google_secret);
        params.add("redirect_uri", null);
        //params.add("redirect_uri", "http://localhost:8080/redirect");
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        RestTemplate rt = new RestTemplate();
        return rt.postForEntity(
                GOOGLE_TOKEN_REQUEST_URL,
                request,
                String.class
        );
    }

    public ResponseEntity<String> requestRefreshedAccessToken(String refresh_token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", google_clientId);
        params.add("client_secret", google_secret);
        params.add("refresh_token", refresh_token);
        params.add("grant_type", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        RestTemplate rt = new RestTemplate();
        return rt.postForEntity(
                GOOGLE_TOKEN_REQUEST_URL,
                request,
                String.class
        );
    }

    public GoogleOAuthTokenDto getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(response.getBody(), GoogleOAuthTokenDto.class);
    }
    public ResponseEntity<Map<String, Object>> requestGoogleUserInfo(String access_token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + access_token);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        return rt.exchange(
                GOOGLE_USERINFO_REQUEST_URL,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });
    }

    public GoogleUserInfo getGoogleUserInfo(String access_token) {
        ResponseEntity<Map<String, Object>> userInfoResponse = requestGoogleUserInfo(access_token);
        Map<String, Object> userInfoMap = userInfoResponse.getBody();
        return new GoogleUserInfo(userInfoMap.get("sub").toString(), userInfoMap.get("email").toString());
    }
}
