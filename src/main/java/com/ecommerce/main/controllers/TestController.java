package com.ecommerce.main.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    
    @GetMapping("/")
    public String test(@RequestParam String p) {
        return "Test endpoint. Param = " + p;
    }
    
}
