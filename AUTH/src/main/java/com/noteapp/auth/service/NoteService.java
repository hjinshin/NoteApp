package com.noteapp.auth.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NoteService {
    @Value("${noteapi_url}")
    private String noteapi_url;

    public void createUserNote(String userId) {
        RestTemplate rt = new RestTemplate();
        String noteapiUrlWithParams = noteapi_url + "/api/database/signUp?userId=" + userId;
        rt.getForObject(noteapiUrlWithParams, String.class);
    }
}
