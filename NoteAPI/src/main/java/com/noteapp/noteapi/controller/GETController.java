package com.noteapp.noteapi.controller;
import com.noteapp.noteapi.dto.AuthResultDto;
import com.noteapp.noteapi.dto.ResponseDto;
import com.noteapp.noteapi.entity.Note;
import com.noteapp.noteapi.service.AuthService;
import com.noteapp.noteapi.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class GETController {
    private final AuthService authService;
    private final NoteService noteService;
    @Autowired
    public GETController(AuthService authService, NoteService noteService) {
        this.authService = authService;
        this.noteService = noteService;
    }

    @GetMapping("/api/database/signUp")
    public void createNote(@RequestParam String userId) {
        try {
            noteService.createEntity(userId);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }
    @GetMapping("/api/database/namelist")
    public ResponseEntity<ResponseDto> getNameList(@RequestParam String access_token) {
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
            return ResponseEntity.ok().body(new ResponseDto(true, authResultDto.getAccess_token(), new HashMap<String, Object>() {{
                put("namelist", noteService.getAllPageNamesByUserId(authResultDto.getUserId()));
            }}));
        } catch (DataAccessException e) {
            return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                put("message", "DB Error");
            }}));
        }
    }
    @GetMapping("/api/database")
    public ResponseEntity<ResponseDto> readAll(@RequestParam String access_token) {
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
            return ResponseEntity.ok().body(new ResponseDto(true,
                    authResultDto.getAccess_token(),
                    new HashMap<String, Object>() {{
                        put("pages", noteService.readAllPages(authResultDto.getUserId()));
                    }}));
        } catch (DataAccessException e) {
            return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                put("message", "DB Error");
            }}));
        }
    }
    @GetMapping("/api/database/page")
    public ResponseEntity<ResponseDto> readPage(@RequestParam String access_token, String page_name) {
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
            Note.Page page = noteService.readPage(authResultDto.getUserId(), page_name);
            if(page == null) {
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", "Page does not exsit");
                }}));
            }
            return ResponseEntity.ok().body(new ResponseDto(true,
                    authResultDto.getAccess_token(),
                    new HashMap<String, Object>() {{
                        put("page", page);
                    }}));
        } catch (DataAccessException e) {
            return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                put("message", "DB Error");
            }}));
        }
    }
}
