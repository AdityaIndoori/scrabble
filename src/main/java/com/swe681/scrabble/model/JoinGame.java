package com.swe681.scrabble.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter //LOMBOK
@Setter //LOMBOK
@NoArgsConstructor //LOMBOK
@Entity
@Table(name="joingame")
public class JoinGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String gameid;
    private String timestamp;

    public JoinGame(String username, String gameid, String timestamp) {
        this.username = username;
        this.gameid = gameid;
        this.timestamp = timestamp;
    }
}
