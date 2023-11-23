package com.noteapp.back.service;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


@Service
public class ImageGenerator {
    public byte[] generateSampleImage() throws IOException {
        // 이미지 크기 및 종이 색상 설정
        int width = 300;
        int height = 300;
        Color paperColor = Color.WHITE;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();

        // 배경을 지정된 색상으로 채우기
        graphics.setColor(paperColor);
        graphics.fillRect(0, 0, width, height);

        // 텍스트 그리기 (예시로 'x = 2'라는 수식)
        graphics.setColor(Color.BLACK);
        graphics.drawString("x = 2", 50, 150);

        // 이미지를 바이트 배열로 변환
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }
}
