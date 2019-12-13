package com.swe681.scrabble.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import com.swe681.scrabble.model.MoveWS;
import com.swe681.scrabble.model.WSOutput;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import com.swe681.scrabble.model.Message;
import com.swe681.scrabble.model.OutputMessage;

import lombok.var;

@Controller
@Slf4j
public class MessageController {

    @MessageMapping("/chat/{gameid}")
    @SendTo("/topic/{gameid}")
    public WSOutput send(@DestinationVariable String gameid, MoveWS move) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        //TODO: VALIDATE MOVE DATA
        //TODO: SAVE MOVE TO DATABASE after VALIDATION
        //TODO: GAME LOGIC HERE SERVICE

        return new WSOutput(move.getWord(), move.getDirection(), move.getRow(), move.getColumn(), move.getGameid(), move.getUsername(), time);
    }
}