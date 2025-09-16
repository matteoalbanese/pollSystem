package com.matalban.pollsystem.api.v0.dto;

import lombok.Data;

import java.util.List;

@Data
public class PollDetails {

    private Integer id;
    private String owner;
    private String expiresAt;
    private String status;
    private WinnerOption winnerOption;
    private List<OptionDto> options;
}
