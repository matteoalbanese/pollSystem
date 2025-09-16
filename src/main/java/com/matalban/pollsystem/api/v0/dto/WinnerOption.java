package com.matalban.pollsystem.api.v0.dto;

import lombok.Data;

@Data
public class WinnerOption {

    private Integer pollId;
    private Integer optionId;
    private Number percentOfWinner;
}
