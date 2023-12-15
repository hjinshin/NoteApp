package com.noteapp.ocr.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImgDto {
    private String access_token;
    private String img;
}
