package com.matalban.pollsystem.services;

import com.matalban.pollsystem.api.v0.dto.PollDetails;

public interface DetailsService {

    PollDetails getPollDetails(Integer id);
}
