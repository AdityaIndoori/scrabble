package com.swe681.scrabble.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.swe681.scrabble.model.Message;
import com.swe681.scrabble.model.OutputMessage;

import lombok.var;

@Controller
public class MessageController {
	
	@Autowired
    HttpSession httpSession;

    @MessageMapping("/chat/{gameid}")
    @SendTo("/topic/{gameid}")
    public OutputMessage send(@DestinationVariable String gameid, final Message message) throws Exception {
    	
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(message.getFrom()+"where gameid is"+gameid, message.getText(), time);
        
    }
    
    
}