package com.swe681.scrabble.model;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter //LOMBOK
@Setter //LOMBOK
@NoArgsConstructor
public class OutputBoard {
    //"word": s_word, "direction": s_direction, "row": s_row, "column":s_column, "gameid":gameid, "username":username
    private List<OutputRow> rows;
    
}