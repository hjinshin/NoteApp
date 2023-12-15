package com.noteapp.noteapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAllDto {
    private String access_token;
    private List<NewPage> new_pages;
    @Data
    public static class NewPage {
        private String name;
        private Map<String, Object> data;
    }
}
