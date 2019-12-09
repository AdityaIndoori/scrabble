package com.swe681.scrabble.controller;

import com.swe681.scrabble.service.StartGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@Controller
public class GameController {

    @Autowired
    StartGameService startGameService;

    @Autowired
    HttpSession httpSession;

    @GetMapping("/startgame")
    public String startgame() {
        try {
            Long id = startGameService.startGame();
            httpSession.setAttribute("gameid",String.valueOf(id));
            return "gameUI";
        } catch (Exception e) {
            return "error";
        }
    }
}
