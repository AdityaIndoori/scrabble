package com.swe681.scrabble.controller;

import com.swe681.scrabble.model.Message;
import com.swe681.scrabble.model.User;
import com.swe681.scrabble.service.SecurityService;
import com.swe681.scrabble.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class TestController {

    @Autowired
    private UserService userService; //Instance of user's database

    @Autowired
    private SecurityService securityService; //For auto login implementation and login implementation

    @GetMapping(value = "/aditya", produces = "application/json")
    public String aditya() {
    	String userName = "Not logged in";
    	if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                userName = ((UserDetails)principal).getUsername();
            }
        }
        return "Server says: In get Request" + "\nUser: " + userName;

    }

    @PostMapping(value = "/aditya")
    public String aditya(@RequestBody String requestBody) {
        log.info("I am here");
        String userName = "Not logged in";
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                userName = ((UserDetails)principal).getUsername();
            }
        }
        return "Server says: In post request " + requestBody + "\nUser: " + userName;
    }
    
    //------------------new registration
    @PostMapping(value = "/aditya/register", consumes = "application/json", produces = "application/json")
    public String registration1(@RequestBody String requestBody) {
        log.info("ADITYA/REGISTER: " + requestBody);
        //userValidator.validate(userForm, bindingResult);

//        if (bindingResult.hasErrors()) {
//            return "registration";
//        }

        JsonParser springParser = JsonParserFactory.getJsonParser();
        Map<String, Object> requestBodyMap = springParser.parseMap(requestBody);

        String userName = (String) requestBodyMap.get("username");
        String password = (String) requestBodyMap.get("password");
        String confirmPassword = (String) requestBodyMap.get("confirmpassword");
        log.info("ADITYA/REGISTER: " + userName + " " + password + " " + confirmPassword);
        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        user.setPasswordConfirm(confirmPassword);
        user.setRoles(new HashSet<>());

        userService.save(user);

        securityService.autoLogin(user.getUsername(), user.getPasswordConfirm());

        return "{\"status\":\"success\"}";
    }
    //------------------

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

    @GetMapping(value = "/aditya2", produces = "application/json")
    public ResponseEntity<List<Message>> aditya2() {
        try {
            List<Message> list = new ArrayList<>();
            Message msg1 = new Message();
            msg1.setFrom("from1");
            msg1.setText("text1");

            Message msg2 = new Message();
            msg2.setFrom("from2");
            msg2.setText("text2");

            list.add(msg1);
            list.add(msg2);

            return new ResponseEntity<>(list, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}