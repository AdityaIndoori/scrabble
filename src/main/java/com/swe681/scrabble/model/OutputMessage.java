package com.swe681.scrabble.model;

import lombok.Getter;
import lombok.Setter;


@Getter //LOMBOK
@Setter //LOMBOK
public class OutputMessage {

	private String from;
    private String text;
    private String time; 
    
    public OutputMessage(final String from, final String text, final String time) {
        this.from = from;
        this.text = text;
        this.time = time;
    }
}