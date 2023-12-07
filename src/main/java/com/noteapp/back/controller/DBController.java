package com.noteapp.back.controller;

import com.noteapp.back.dto.UpdateAllDto;
import com.noteapp.back.model.GoogleUserInfo;
import com.noteapp.back.dto.ResponseDto;
import com.noteapp.back.dto.PageUpdateDto;
import com.noteapp.back.service.GoogleOAuthService;
import com.noteapp.back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@RestController
public class DBController {
    @Autowired
    private UserService userService;
    @Autowired
    private GoogleOAuthService googleOAuthService;

    @GetMapping("/api/test")
    public String testGoogleAccessToken(@RequestParam String access_token) {
        try {
            GoogleUserInfo googleUserInfo = googleOAuthService.getGoogleUserInfo(access_token);
            return googleUserInfo.toString();
        } catch (HttpStatusCodeException e) {
            return e.getMessage();
        }
    }

    @GetMapping("/api/database/namelist")
    public ResponseEntity<ResponseDto> getNameList(@RequestParam String access_token) {
        try {
            GoogleUserInfo googleUserInfo = googleOAuthService.getGoogleUserInfo(access_token);
            String userId = googleUserInfo.getId();
            return ResponseEntity.ok().body(new ResponseDto(true,
                    googleOAuthService.tokenRefreshing(userId),
                    new HashMap<String, Object>() {{
                        put("namelist", userService.getAllPageNamesByUserId(userId));
                    }}));
        } catch (HttpStatusCodeException | IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                put("message", "Access failed: invalid access");
            }}));
        }

    }

    @PostMapping("/api/database")
    public ResponseEntity<ResponseDto> updateAll(@RequestBody UpdateAllDto updateAllDto) {
        try {
            GoogleUserInfo googleUserInfo = googleOAuthService.getGoogleUserInfo(updateAllDto.getAccess_token());
            String userId = googleUserInfo.getId();
            if(!userService.existUserId(userId)) {
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", "User does not exist");
                }}));
            }
            userService.updateAll(userId, updateAllDto.getNew_pages());
            return ResponseEntity.ok().body(new ResponseDto(true,
                    googleOAuthService.tokenRefreshing(userId),
                    new HashMap<String, Object>() {{
                        put("message", "update success");
                    }}));
        } catch (HttpStatusCodeException | IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                put("message", "Update failed: invalid access");
            }}));
        }
    }
    @GetMapping("/api/database")
    public ResponseEntity<ResponseDto> readAll(@RequestParam String access_token) {
        try {
            GoogleUserInfo googleUserInfo = googleOAuthService.getGoogleUserInfo(access_token);
            String userId = googleUserInfo.getId();
            if(!userService.existUserId(userId))
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", "User does not exist");
                }}));
            return ResponseEntity.ok().body(new ResponseDto(true,
                    googleOAuthService.tokenRefreshing(userId),
                    new HashMap<String, Object>() {{
                        put("pages", userService.readUserData(userId));
                    }}));
        } catch (HttpStatusCodeException | IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                put("message", "read failed: invalid access");
            }}));
        }
    }

    @DeleteMapping("/api/database")
    public ResponseEntity<ResponseDto> deleteUser(@RequestParam String access_token) {
        try {
            GoogleUserInfo googleUserInfo = googleOAuthService.getGoogleUserInfo(access_token);
            String userId = googleUserInfo.getId();
            if(!userService.existUserId(userId))
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", "User does not exist");
                }}));
            userService.deleteUser(userId);
            return ResponseEntity.ok().body(new ResponseDto(true,
                    googleOAuthService.tokenRefreshing(userId),
                    new HashMap<String, Object>() {{
                        put("message", "delete success");
                    }}));
        } catch (HttpStatusCodeException | IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                put("message", "delete failed: invalid access");
            }}));
        }
    }



    @PostMapping("/api/database/page")
    public ResponseEntity<ResponseDto> updatePage(@RequestBody PageUpdateDto pageUpdateDto) {
        try {
            GoogleUserInfo googleUserInfo = googleOAuthService.getGoogleUserInfo(pageUpdateDto.getAccess_token());
            String userId = googleUserInfo.getId();
            String oldPageName = pageUpdateDto.getOld_page_name();
            if(!userService.existUserId(userId)) {
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", "User does not exist");
                }}));
            }
            if(oldPageName != null && !userService.existPageName(userId, oldPageName))
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", "Page does not exist");
                }}));
            if(!Objects.equals(oldPageName, pageUpdateDto.getNew_page().getName())) {
                if(userService.existPageName(userId, pageUpdateDto.getNew_page().getName()))
                    return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                        put("message", "Page already exists");
                    }}));
            }
            userService.pageUpdate(userId, oldPageName, pageUpdateDto.getNew_page());
            return ResponseEntity.ok().body(new ResponseDto(true,
                    googleOAuthService.tokenRefreshing(userId),
                    new HashMap<String, Object>() {{
                put("message", "update success");
            }}));
        } catch (HttpStatusCodeException | IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                put("message", "Update failed: invalid access");
            }}));
        }
    }

    @GetMapping("/api/database/page")
    public ResponseEntity<ResponseDto> readPage(@RequestParam String access_token, String page_name) {
        try {
            GoogleUserInfo googleUserInfo = googleOAuthService.getGoogleUserInfo(access_token);
            String userId = googleUserInfo.getId();
            if(!userService.existUserId(userId))
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", "User does not exist");
                }}));
            return ResponseEntity.ok().body(new ResponseDto(true,
                    googleOAuthService.tokenRefreshing(userId),
                    new HashMap<String, Object>() {{
                        put("page", userService.readPage(userId, page_name));
                    }}));
        } catch (HttpStatusCodeException | IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                put("message", "read failed: invalid access");
            }}));
        }
    }

    @DeleteMapping("/api/database/page")
    public ResponseEntity<ResponseDto> deletePage(@RequestParam String access_token, String page_name) {
        try {
            GoogleUserInfo googleUserInfo = googleOAuthService.getGoogleUserInfo(access_token);
            String userId = googleUserInfo.getId();
            if(!userService.existUserId(userId))
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", "User does not exist");
                }}));
            if(!userService.existPageName(userId, page_name))
                return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                    put("message", "Page does not exist");
                }}));
            userService.deletePageByName(userId, page_name);
            return ResponseEntity.ok().body(new ResponseDto(true,
                    googleOAuthService.tokenRefreshing(userId),
                    new HashMap<String, Object>() {{
                put("message", "delete success");
            }}));
        } catch (HttpStatusCodeException | IOException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.ok().body(new ResponseDto(false, null, new HashMap<String, Object>() {{
                put("message", "delete failed: invalid access");
            }}));
        }
    }
}

