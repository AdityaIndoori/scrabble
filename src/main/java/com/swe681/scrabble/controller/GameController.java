package com.swe681.scrabble.controller;

import com.swe681.scrabble.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class GameController {

    @Autowired
    GameService gameService;

    @Autowired
    HttpSession httpSession;

    @GetMapping("/startgame")
    public String startgame() {
        try {
            Long id = gameService.startGame();
            httpSession.setAttribute("gameid",String.valueOf(id));
            return "gameUI";
        } catch (Exception e) {
            httpSession.setAttribute("error","There was an error, please try again");
            return "redirect:/welcome";
        }
    }

    @GetMapping("/joingame")
    public String joingame() {
        try {
            Long id = gameService.joinGame();
            httpSession.setAttribute("gameid",String.valueOf(id));
            return "gameUI";
        } catch (Exception e) {
            httpSession.setAttribute("error","There was an error, please try again");
            return "redirect:/welcome";
        }
    }

}
