package com.swe681.scrabble.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter //LOMBOK
@Setter //LOMBOK
@NoArgsConstructor //LOMBOK

public class JoinableGame {

    private String username;
    private String gameid;
    private String timestamp;

    public JoinableGame(String username, String gameid, String timestamp) {
        this.username = username;
        this.gameid = gameid;
        this.timestamp = timestamp;
    }
}
