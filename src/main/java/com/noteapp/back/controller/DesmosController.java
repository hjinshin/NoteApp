package com.noteapp.back.controller;

import com.noteapp.back.service.DesmosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DesmosController {
    @Autowired
    private DesmosService desmosService;


}
