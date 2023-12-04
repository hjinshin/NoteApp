//package com.noteapp.back.controller;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//@RestController
//public class GoogleController {
//    @Value("${google_clientId}")
//    private String google_clientId;
//    @Value("${google_redirectUrl}")
//    private String google_redirectUrl;
//    @Value("${google_secret}")
//    private String google_secret;
//    @GetMapping("/redirect")
//
//    //    Google 로그인 Redirect
//    public String handleGoogleCallback(@RequestParam("code") String code) {
//        // Exchange authorization code for access token
//        String tokenEndpoint = "https://accounts.google.com/o/oauth2/token";
//        String tokenRequest = "code=" + code
//                + "&client_id=" + google_clientId
//                + "&client_secret=" + google_secret
//                + "&redirect_uri=" + google_redirectUrl
//                + "&grant_type=authorization_code";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/x-www-form-urlencoded");
//
//        HttpEntity<String> request = new HttpEntity<>(tokenRequest, headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.exchange(
//                tokenEndpoint,
//                HttpMethod.POST,
//                request,
//                String.class
//        );
//
//        return response.getBody();
//    }
//}
