package com.noteapp.back.controller;

import com.noteapp.back.model.GoogleUserInfo;
import com.noteapp.back.dto.ImgDto;
import com.noteapp.back.dto.ResponseDto;
import com.noteapp.back.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

@RestController
public class MathPixController {
    @Autowired
    private ImageGenerator imageGenerator;
    @Autowired
    private MathPixService mathPixService;
    @Autowired
    private GoogleOAuthService googleOAuthService;
    @Autowired
    private UserService userService;

    @PostMapping("/api/mathpix")
    public ResponseEntity<ResponseDto> recognizeMathExpression(@RequestBody ImgDto imgDto) throws URISyntaxException {
        try {
            GoogleUserInfo googleUserInfo = googleOAuthService.getGoogleUserInfo(imgDto.getAccess_token());
            if(!userService.existUserId(googleUserInfo.getId())) {
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", "User does not exist");
                }}));
            }
            return ResponseEntity.ok().body(new ResponseDto(true,
                    googleOAuthService.tokenRefreshing(googleUserInfo.getId()),
                    new HashMap<String, Object>() {{
                    put("img", mathPixService.recognizeMathExpression(imgDto.getImg()));
                }}));

        } catch (HttpStatusCodeException | IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                put("message", "MathPix Error: invalid access");
            }}));
        }
    }

    @PostMapping("/api/mathpixtest")
    public String recognizeMathExpression() throws URISyntaxException, IOException {
        System.out.println("this is test");

        String temp = java.util.Base64.getEncoder().encodeToString(imageGenerator.generateSampleImage()); // 샘플 이미지
        return mathPixService.recognizeMathExpression(temp);
    }

}
