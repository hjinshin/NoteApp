package com.noteapp.ocr.controller;

import com.noteapp.ocr.dto.AuthResultDto;
import com.noteapp.ocr.dto.ImgDto;
import com.noteapp.ocr.dto.ResponseDto;
import com.noteapp.ocr.service.*;
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
    private final ImageGenerator imageGenerator;
    private final MathPixService mathPixService;
    private final AuthService authService;
    @Autowired
    public MathPixController(ImageGenerator imageGenerator, MathPixService mathPixService, AuthService authService) {
        this.imageGenerator = imageGenerator;
        this.mathPixService = mathPixService;
        this.authService = authService;
    }

    @PostMapping("/api/mathpix")
    public ResponseEntity<ResponseDto> recognizeMathExpression(@RequestBody ImgDto imgDto) {
        try {
            // 인증 서버에 인증 요청
            AuthResultDto authResultDto = authService.authAccessToken(imgDto.getAccess_token());
            if(!authResultDto.isSuccess()) {
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", authResultDto.getMessage());
                }}));
            }
            return ResponseEntity.ok().body(new ResponseDto(true,
                    authResultDto.getAccess_token(),
                    new HashMap<String, Object>() {{
                        put("img", mathPixService.recognizeMathExpression(imgDto.getImg()));
                    }}));

        } catch (HttpStatusCodeException | URISyntaxException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
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
