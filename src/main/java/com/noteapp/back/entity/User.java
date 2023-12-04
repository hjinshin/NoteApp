package com.noteapp.back.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    private ObjectId id;
    @Indexed(unique = true)
    private String userId;
    private String email;
    private Map<String, Object> data;
    private LocalDateTime last_modified_date;


    public User(String userId, String email, Map<String, Object> data, LocalDateTime last_modified_date) {
        this.userId = userId;
        this.email = email;
        this.data = data;
        this.last_modified_date = last_modified_date;
    }

}
