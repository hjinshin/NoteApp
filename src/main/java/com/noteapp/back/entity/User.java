package com.noteapp.back.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private String userId;
    private String email;
    private List<Page> pages;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Page {
        private String name;
        private Map<String, Object> data;
        private LocalDateTime last_modified_date;
    }
}
