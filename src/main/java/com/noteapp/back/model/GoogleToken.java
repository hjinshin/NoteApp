package com.noteapp.back.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoogleToken {
    private String id;
    private String refresh_token;
    private String access_token;
}
