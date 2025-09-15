package com.matalban.pollsystem.services;

import com.matalban.pollsystem.api.v0.dto.PollDto;
import org.springframework.data.domain.Page;


public interface PollService {

    PollDto createPoll(PollDto pollDto);
    Page<PollDto> getPollListPage(Integer pageParam, Integer sizeParam);
    PollDto getPoll(Integer pollId);
    void deletePoll(Integer pollId);

    PollDto updatePoll(Integer id, PollDto pollDto);




}
