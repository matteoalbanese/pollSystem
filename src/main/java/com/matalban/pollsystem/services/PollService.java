package com.matalban.pollsystem.services;

import com.matalban.pollsystem.api.v0.dto.PollDto;
import com.matalban.pollsystem.api.v0.dto.PollListPage;


public interface PollService {

    PollDto createPoll(PollDto pollDto);
    PollListPage getPollListPage(Integer pageParam, Integer sizeParam);
    PollDto getPoll(Integer pollId);
    void deletePoll(Integer pollId);

    PollDto updatePoll(Integer id, PollDto pollDto);




}
