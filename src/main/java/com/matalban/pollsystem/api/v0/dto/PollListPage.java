package com.matalban.pollsystem.api.v0.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PollListPage extends Page {
    List<PollDto> PollList;
}
