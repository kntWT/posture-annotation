package com.example.api.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class ApiController {
    @GetMapping("/")
    public String index() {
        return "Hello, World!";
    }
}
