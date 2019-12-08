package com.swe681.scrabble.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

@RestController
public class TestController {

    @GetMapping(value = "/aditya", produces = "application/json")
    public String aditya() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (userDetails instanceof UserDetails) {
            return ((UserDetails) userDetails).getUsername();
        }
        return "Greetings from Spring Boot!";
    }

    @PostMapping(value = "/aditya", consumes = "application/json", produces = "application/json")
    public String aditya(@RequestBody String requestBody) {
        return "{Server says: " + requestBody + "}";
    }

    @RequestMapping(value = "/aditya/{id}", produces = "application/json")
    public ResponseEntity<String> adityaId(@PathVariable String id) {
        try {
            if (Integer.parseInt(id) < 10)
                return new ResponseEntity<String>("From Server: " + id, HttpStatus.OK);
            else
                return new ResponseEntity<String>("{\"Error\":\"No such page\"}", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
    }

}