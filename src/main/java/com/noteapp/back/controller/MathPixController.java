package com.noteapp.back.controller;

import com.noteapp.back.dto.ImgDto;
import com.noteapp.back.service.ImageGenerator;
import com.noteapp.back.service.MathPixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class MathPixController {
    @Autowired
    private ImageGenerator imageGenerator;
    @Autowired
    private MathPixService mathPixService;

    @PostMapping("/api/mathpix")
    public String recognizeMathExpression(@RequestBody ImgDto imgDto) throws URISyntaxException {
        // access_token으로 회원가입여부 확인
        return mathPixService.recognizeMathExpression(imgDto.getImg());
    }

    @PostMapping("/api/mathpixtest")
    public String recognizeMathExpression() throws URISyntaxException, IOException {
        System.out.println("this is test");

        String temp = java.util.Base64.getEncoder().encodeToString(imageGenerator.generateSampleImage()); // 샘플 이미지
        return mathPixService.recognizeMathExpression(temp);
    }
}
