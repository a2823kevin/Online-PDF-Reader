package com.a2823kevin.pdfreader.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class WebsiteController {
    @GetMapping({
        "/", 
        "/bookshelf", 
        "/dashboard", 
        "/reader/*"
    })
    public String site() {
        return "forward:/index.html";
    }
}
