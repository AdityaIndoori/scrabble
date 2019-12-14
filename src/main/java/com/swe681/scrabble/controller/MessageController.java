package com.swe681.scrabble.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.swe681.scrabble.service.JoinGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.swe681.scrabble.model.MoveWS;
import com.swe681.scrabble.model.OutputMove;
import com.swe681.scrabble.model.WSOutput;
import com.swe681.scrabble.service.GameLogicService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MessageController {
	
	@Autowired
	GameLogicService gameLogicService;

	@Autowired
    JoinGameService joinGameService;

    @MessageMapping("/chat/{gameid}")
    @SendTo("/topic/{gameid}")
    public OutputMove send(@DestinationVariable String gameid, MoveWS move) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        joinGameService.onMoveSubmit(move.getGameid(), move.getUsername());
        //TODO: VALIDATE MOVE DATA
        //TODO: SAVE MOVE TO DATABASE after VALIDATION
        //TODO: GAME LOGIC HERE SERVICE
        
        String response = gameLogicService.playMove(move);
        log.info("In message controller: after playing move------------------response:"+response);
        
        try {
        if(response.contains("SUCCESS")) {
			
			move.setWord(move.getWord().toUpperCase());
			
			WSOutput wsout = new WSOutput(move.getWord(), move.getDirection(), move.getRow(), move.getColumn(), move.getGameid(), move.getUsername(), time);
			
			OutputMove om = ((GameLogicService) gameLogicService).settingOutputMove(move);
			
			om.setWsoutput(wsout);
			
			return om;
			
        }}catch(Exception ex) {
        	throw ex;
        }
        return null;

        //return new WSOutput(move.getWord(), move.getDirection(), move.getRow(), move.getColumn(), move.getGameid(), move.getUsername(), time);
    }
}