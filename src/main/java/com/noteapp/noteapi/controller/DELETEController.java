package com.noteapp.noteapi.controller;

import com.noteapp.noteapi.dto.AuthResultDto;
import com.noteapp.noteapi.dto.ResponseDto;
import com.noteapp.noteapi.service.AuthService;
import com.noteapp.noteapi.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class DELETEController {
    private final AuthService authService;
    private final NoteService noteService;
    @Autowired
    public DELETEController(AuthService authService, NoteService noteService) {
        this.authService = authService;
        this.noteService = noteService;
    }
    @DeleteMapping("/api/database")
    public ResponseEntity<ResponseDto> deleteAllPages(@RequestParam String access_token) {
        try {
            // 인증 서버에 인증 요청
            AuthResultDto authResultDto = authService.authAccessToken(access_token);
            if(!authResultDto.isSuccess()) {
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", authResultDto.getMessage());
                }}));
            }
            if(!noteService.existUserId(authResultDto.getUserId())) {
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", "User does not exist");
                }}));
            }
            // Success
            noteService.deleteAllPages(authResultDto.getUserId());
            return ResponseEntity.ok().body(new ResponseDto(true,
                    authResultDto.getAccess_token(),
                    new HashMap<String, Object>() {{
                        put("message", "delete success");
                    }}));
        } catch (DataAccessException e) {
            return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                put("message", "DB Error");
            }}));
        }
    }
    @DeleteMapping("/api/database/page")
    public ResponseEntity<ResponseDto> deletePage(@RequestParam String access_token, String page_name) {
        try {
            // 인증 서버에 인증 요청
            AuthResultDto authResultDto = authService.authAccessToken(access_token);
            if(!authResultDto.isSuccess()) {
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", authResultDto.getMessage());
                }}));
            }
            if(!noteService.existUserId(authResultDto.getUserId())) {
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", "User does not exist");
                }}));
            }
            // Success
            noteService.deletePageByName(authResultDto.getUserId(), page_name);
            return ResponseEntity.ok().body(new ResponseDto(true,
                    authResultDto.getAccess_token(),
                    new HashMap<String, Object>() {{
                        put("message", "delete success");
                    }}));
        } catch (DataAccessException e) {
            return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                put("message", "DB Error");
            }}));
        }
    }
}
