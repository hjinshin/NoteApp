package com.noteapp.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDto {
    private String userId;
    private Map<String, Object> info;
}
