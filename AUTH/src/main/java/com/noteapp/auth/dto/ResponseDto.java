package com.noteapp.auth.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ResponseDto {
    private boolean success;
    private String access_token;
    private Map<String, Object> response;
}

