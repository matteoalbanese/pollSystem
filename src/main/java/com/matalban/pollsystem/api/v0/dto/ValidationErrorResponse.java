package com.matalban.pollsystem.api.v0.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ValidationErrorResponse {
    private String timestamp;
    private String message;
    private String path;
}
