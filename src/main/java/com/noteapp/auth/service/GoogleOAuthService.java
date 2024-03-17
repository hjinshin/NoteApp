package com.noteapp.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noteapp.auth.dto.GoogleOAuthTokenDto;
import com.noteapp.auth.dto.ResponseDto;
import com.noteapp.auth.model.GoogleUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
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
    private final NoteService noteService;
    private final String GOOGLE_TOKEN_REQUEST_URL = "https://oauth2.googleapis.com/token";
    private final String GOOGLE_TOKEN_REVOKE_URL = "https://oauth2.googleapis.com/revoke";
    private final String GOOGLE_USERINFO_REQUEST_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

    public GoogleOAuthService(UserService userService, TokenService tokenService, NoteService noteService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.noteService = noteService;
    }

    public ResponseEntity<ResponseDto> googleLogin(String code) throws IOException, HttpStatusCodeException {
        ResponseEntity<String> accessTokenResponse = requestAccessToken(code);
        if(accessTokenResponse.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                put("message", "Login failed: invalid code");
            }}));
        }
        GoogleOAuthTokenDto oAuthTokenDto = getAccessToken(accessTokenResponse);
        // acccess_token으로 userInfo 가져오기
        String accessToken = oAuthTokenDto.getAccess_token();
        GoogleUserInfo googleUserInfo = getGoogleUserInfo(accessToken);
        // User 및 Token 테이블에 개체 추가
        String userId = googleUserInfo.getId();
        if(!userService.existUserId(userId)) {
            userService.signUp(userId, googleUserInfo.getEmail(), oAuthTokenDto.getRefresh_token());
            tokenService.signUp(userId, accessToken, oAuthTokenDto.getExpires_in());
            noteService.createUserNote(userId);
        }
        if(oAuthTokenDto.getRefresh_token() != null) {
            userService.updateRefreshToken(userId, oAuthTokenDto.getRefresh_token());
        }
        tokenService.update(userId, oAuthTokenDto.getAccess_token(), oAuthTokenDto.getExpires_in());
        return ResponseEntity.ok().body(new ResponseDto(true, accessToken, new HashMap<String, Object>() {{
            put("message", "Login success");
        }}));
    }
    public ResponseEntity<String> requestAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", google_clientId);
        params.add("client_secret", google_secret);
        params.add("redirect_uri", null);
        //params.add("redirect_uri", "http://localhost:8080/login");
        params.add("grant_type", "authorization_code");

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
    public GoogleUserInfo getGoogleUserInfo(String access_token) {
        ResponseEntity<Map<String, Object>> userInfoResponse = requestGoogleUserInfo(access_token);
        Map<String, Object> userInfoMap = userInfoResponse.getBody();
        return new GoogleUserInfo(userInfoMap.get("sub").toString(), userInfoMap.get("email").toString());
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
    public String refreshAccessToken(String accessToken) throws IOException {
        String userId = tokenService.findUserIdByAccessToken(accessToken);
        if(userId == null)  return null;
        String refreshToken = userService.findRefreshTokenByUserId(userId);
        ResponseEntity<String> accessTokenResponse = requestRefreshedAccessToken(refreshToken);
        GoogleOAuthTokenDto oAuthTokenDto = getAccessToken(accessTokenResponse);
        String newAccessToken = oAuthTokenDto.getAccess_token();
        tokenService.deleteToken(userId);
        tokenService.signUp(userId, newAccessToken, oAuthTokenDto.getExpires_in());
        return newAccessToken;
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
    public ResponseEntity<String> requestRefreshToken(String accessToken) {
        RestTemplate rt = new RestTemplate();
        String revokeUrlWithParams = GOOGLE_TOKEN_REVOKE_URL + "?token=" + accessToken;
        return rt.postForEntity(revokeUrlWithParams, null, String.class);

    }
}
