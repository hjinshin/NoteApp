package com.noteapp.back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageUpdateDto {
    private String access_token;
    private String old_page_name;
    private NewPage new_page;
        @Data
        public static class NewPage {
            private String name;
            private Map<String, Object> data;
        }
}
