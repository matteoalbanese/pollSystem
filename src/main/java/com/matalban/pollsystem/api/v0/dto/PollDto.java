package com.matalban.pollsystem.api.v0.dto;

import com.matalban.pollsystem.domain.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PollDto {

    private String owner;
    private Integer id;
    private String question;
    private String expiresAt;
    private Status status;
}
