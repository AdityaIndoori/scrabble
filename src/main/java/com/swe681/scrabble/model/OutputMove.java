package com.swe681.scrabble.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minidev.json.JSONArray;


@Getter //LOMBOK
@Setter //LOMBOK
@NoArgsConstructor
public class OutputMove {
    //"word": s_word, "direction": s_direction, "row": s_row, "column":s_column, "gameid":gameid, "username":username
    private JSONArray json;
    private String error;
    private String gameRack;
    private String p1Username;
    private Integer p1Score;
    private String p2Username;
    private Integer p2Score;
    private WSOutput wsoutput;
    private List<List<String>> outputTable;
}
