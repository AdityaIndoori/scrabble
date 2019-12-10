package com.swe681.scrabble.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter //LOMBOK
@Setter //LOMBOK
@NoArgsConstructor
public class WSOutput {
    private String word;
    private String direction;
    private String row;
    private String column;
    private String gameid;
    private String username;
    private String time;

    public WSOutput(String word, String direction, String row, String column, String gameid, String username, String time) {
        this.word = word;
        this.direction = direction;
        this.row = row;
        this.column = column;
        this.gameid = gameid;
        this.username = username;
        this.time = time;
    }
}
