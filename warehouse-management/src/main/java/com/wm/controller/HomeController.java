package com.wm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home") // http://localhost:8080/home
    public String home() {
        return "home"; // loads templates/home.html
    }

}