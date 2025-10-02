package com.matalban.pollsystem.api.v0.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.matalban.pollsystem.domain.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PollDto {


    private String owner;
    private Integer id;
    private String question;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date expiresAt;
    private Status status;
}
