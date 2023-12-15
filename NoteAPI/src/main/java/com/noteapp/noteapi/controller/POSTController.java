package com.noteapp.noteapi.controller;

import com.noteapp.noteapi.dto.AuthResultDto;
import com.noteapp.noteapi.dto.PageUpdateDto;
import com.noteapp.noteapi.dto.ResponseDto;
import com.noteapp.noteapi.dto.UpdateAllDto;
import com.noteapp.noteapi.service.AuthService;
import com.noteapp.noteapi.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Objects;

@RestController
public class POSTController {
    private final AuthService authService;
    private final NoteService noteService;
    @Autowired
    public POSTController(AuthService authService, NoteService noteService) {
        this.authService = authService;
        this.noteService = noteService;
    }

    @PostMapping("/api/database/page")
    public ResponseEntity<ResponseDto> updatePage(@RequestBody PageUpdateDto pageUpdateDto) {
        try {
            // 인증 서버에 인증 요청
            AuthResultDto authResultDto = authService.authAccessToken(pageUpdateDto.getAccess_token());
            if(!authResultDto.isSuccess()) {
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", authResultDto.getMessage());
                }}));
            }
            String userId = authResultDto.getUserId();
            String oldPageName = pageUpdateDto.getOld_page_name();
            if(!noteService.existUserId(userId)) {
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", "User does not exist");
                }}));
            }
            if(oldPageName != null && !noteService.existPageName(userId, oldPageName)) {
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", "Page does not exist");
                }}));
            }
            if(!Objects.equals(oldPageName, pageUpdateDto.getNew_page().getName())) {
                if(noteService.existPageName(userId, pageUpdateDto.getNew_page().getName()))
                    return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                        put("message", "Page already exists");
                    }}));
            }
            noteService.pageUpdate(userId, oldPageName, pageUpdateDto.getNew_page());
            return ResponseEntity.ok().body(new ResponseDto(true,
                    authResultDto.getAccess_token(),
                    new HashMap<String, Object>() {{
                        put("message", "update success");
                    }}));
        } catch (DataAccessException e) {
            return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                put("message", "DB Error");
            }}));
        }
    }

    @PostMapping("/api/database")
    public ResponseEntity<ResponseDto> updateAll(@RequestBody UpdateAllDto updateAllDto) {
        try {
            // 인증 서버에 인증 요청
            AuthResultDto authResultDto = authService.authAccessToken(updateAllDto.getAccess_token());
            if(!authResultDto.isSuccess()) {
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", authResultDto.getMessage());
                }}));
            }
            String userId = authResultDto.getUserId();
            if(!noteService.existUserId(userId)) {
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", "User does not exist");
                }}));
            }
            noteService.updateAll(userId, updateAllDto.getNew_pages());
            return ResponseEntity.ok().body(new ResponseDto(true,
                    authResultDto.getAccess_token(),
                    new HashMap<String, Object>() {{
                        put("message", "update success");
                    }}));
        } catch (DataAccessException e) {
            return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                put("message", "DB Error");
            }}));
        }
    }
}
