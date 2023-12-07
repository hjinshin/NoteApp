package com.noteapp.back.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    @Id
    private String userId;
    @Indexed(unique = true)
    private String refreshToken;
    @Indexed(unique = true)
    private String accessToken;
}
