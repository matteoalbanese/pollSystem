package com.matalban.pollsystem.api.v0.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Page {

    private Boolean first;
    private Boolean last;
    private Integer size;
    private Integer totalElements;
    private Integer totalPages;
    private Integer number;
}
