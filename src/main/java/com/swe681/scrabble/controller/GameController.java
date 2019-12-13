package com.swe681.scrabble.controller;

import com.swe681.scrabble.model.JoinGame;
import com.swe681.scrabble.service.GameService;
import com.swe681.scrabble.service.JoinGameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class GameController {

    @Autowired
    GameService gameService;

    @Autowired
    JoinGameService joinGameService;

    @Autowired
    HttpSession httpSession;

    @GetMapping("/startgame")
    public String startgame() {
        try {
            Long id = gameService.startGame();
            httpSession.setAttribute("gameid",String.valueOf(id));
            httpSession.setAttribute("error",null);
            return "redirect:/gameui";
        } catch (Exception e) {
            httpSession.setAttribute("error","error");
            return "redirect:/welcome";
        }
    }

    @GetMapping("/joingame")
    public String joingame() {
        try {
            Long id = gameService.joinGame();
            httpSession.setAttribute("gameid",String.valueOf(id));
            httpSession.setAttribute("error",null);
            return "redirect:/gameui";
        } catch (Exception e) {
            httpSession.setAttribute("error","error");
            return "redirect:/welcome";
        }
    }

    @GetMapping({"/gameui"})
    public String gameUI(Model model) {
        if(httpSession.getAttribute("gameid")==null){
            httpSession.setAttribute("error","error");
            return "redirect:/welcome";
        }
        return "gameUI";
    }

    @GetMapping("/leave")
    public String leaveGame(JoinGame joinGameInput) {
        try {
            joinGameService.saveToDatabase();
            httpSession.setAttribute("error",null);
            return "redirect:/welcome";
        } catch (Exception e) {
            httpSession.setAttribute("error","error");
            return "redirect:/welcome";
        }
    }

    @GetMapping("/rejoingame/{gameid}")
    public String rejoingame(@PathVariable String gameid) {
        try {
            log.info(String.format("rejoingame: Game ID is %s", gameid));
            if(gameService.rejoinGame(gameid)) {
                httpSession.setAttribute("gameid", gameid);
                httpSession.setAttribute("error", null);
                return "redirect:/gameui";
            }
            else {
                httpSession.setAttribute("error","error");
                return "redirect:/welcome";
            }
        } catch (Exception e) {
            httpSession.setAttribute("error","error");
            return "redirect:/welcome";
        }
    }
}