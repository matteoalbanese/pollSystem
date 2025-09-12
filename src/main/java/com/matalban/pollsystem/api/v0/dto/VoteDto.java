package com.matalban.pollsystem.api.v0.dto;

import lombok.Data;

@Data
public class VoteDto {

    private Integer optionId;
    private Integer id;
    private String votedAt;

}
