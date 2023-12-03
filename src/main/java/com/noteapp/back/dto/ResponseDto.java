package com.noteapp.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ResponseDto {
    private boolean success;
    private Map<String, Object> response;
}
