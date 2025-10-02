package com.matalban.pollsystem.api.v0.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.matalban.pollsystem.domain.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PollDto {



    @NotBlank
    private String owner;
    private Integer id;

    @NotBlank
    private String question;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private Date expiresAt;


    @NotNull
    private Status status;
}
