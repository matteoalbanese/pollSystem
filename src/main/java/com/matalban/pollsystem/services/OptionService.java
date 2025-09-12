package com.matalban.pollsystem.services;

import com.matalban.pollsystem.api.v0.dto.OptionDto;
import com.matalban.pollsystem.api.v0.dto.VoteDto;

public interface OptionService {

    OptionDto insertOption(Integer id, OptionDto optionDto);
    void deleteOption(Integer pollId, Integer optionId);
    OptionDto updateOption(Integer pollId, Integer optionId, OptionDto optionDto);
    OptionDto getOption(Integer pollId, Integer optionId);

    OptionDto vote(Integer pollId, Integer optionId);
    VoteDto getVote(Integer pollId);

}
