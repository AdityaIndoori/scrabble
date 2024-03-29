package com.swe681.scrabble.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.swe681.scrabble.model.User;
import com.swe681.scrabble.service.SecurityService;
import com.swe681.scrabble.service.UserService;
import com.swe681.scrabble.validation.UserValidator;

@Controller
public class UserController {
	
	@Autowired
	HttpSession httpSession;
	
    @Autowired
    private UserService userService; //Instance of user's database

    @Autowired
    private SecurityService securityService; //For auto login implementation and login implementation

    @Autowired
    private UserValidator userValidator; //For validation of user inputs

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/welcome";
    }


    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "Your username and password is invalid.");
        }

        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }

        return "login";
    }

    @GetMapping({"/", "/welcome"})
    public String welcome(Model model) {
        return "welcome";
    }
    
    
    @GetMapping({"/welcome/websocket/chat", "/websocket/chat"})
    public String test(Model model) {
        return "redirect:/chat";
    }

    @GetMapping({"/chat"})
    public String chat(Model model) {
        return "message";
    }
}
