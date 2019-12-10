package com.swe681.scrabble.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter //LOMBOK
@Setter //LOMBOK
@NoArgsConstructor
public class MoveWS {
    //"word": s_word, "direction": s_direction, "row": s_row, "column":s_column, "gameid":gameid, "username":username
    private String word;
    private String direction;
    private String row;
    private String column;
    private String gameid;
    private String username;
}
