package com.matalban.pollsystem.api.v0.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.matalban.pollsystem.domain.Status;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PollDetails {

    private Integer id;
    private String owner;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date expiresAt;
    private Status status;
    private WinnerOption winnerOption;
    private List<OptionDto> options;
}
