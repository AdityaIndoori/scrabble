package com.swe681.scrabble.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/aditya")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}