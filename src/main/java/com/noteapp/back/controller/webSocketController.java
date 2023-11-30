package com.noteapp.back.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class webSocketController {
    @Value("${server_url}")
    private String server_url;

    @RequestMapping(value = "/desmos")
    public String desmosPage(Model model) {
        model.addAttribute("server_url", server_url);
        return "desmos";
    }
}
