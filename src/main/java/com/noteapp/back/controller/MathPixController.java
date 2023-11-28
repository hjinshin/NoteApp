package com.noteapp.back.controller;

import com.noteapp.back.dto.ImgDto;
import com.noteapp.back.service.ImageGenerator;
import com.noteapp.back.service.MathPixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String recognizeMathExpression(@RequestParam ImgDto imgDto) throws URISyntaxException {
        System.out.println(imgDto);

        return mathPixService.recognizeMathExpression(imgDto.getImg());
    }

    @PostMapping("/api/mathpixtest")
    public String recognizeMathExpression() throws URISyntaxException, IOException {
        System.out.println("this is test");

        String temp = java.util.Base64.getEncoder().encodeToString(imageGenerator.generateSampleImage()); // 샘플 이미지
        return mathPixService.recognizeMathExpression(temp);
    }
}
