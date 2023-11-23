package com.noteapp.back.controller;

import com.noteapp.back.service.ImageGenerator;
import com.noteapp.back.service.MathPixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/api/mathpix")
    public String recognizeMathExpression(@RequestParam String test) throws URISyntaxException, IOException {
        System.out.println(test);

        byte[] temp = imageGenerator.generateSampleImage(); // 샘플 이미지
        return mathPixService.recognizeMathExpression(temp);
    }
}
