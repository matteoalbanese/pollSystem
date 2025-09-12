package com.matalban.pollsystem.api.v0.mappers;

import com.matalban.pollsystem.api.v0.dto.VoteDto;
import com.matalban.pollsystem.domain.Vote;
import org.springframework.stereotype.Component;

@Component
public class VoteMapper {



    public VoteDto voteToVoteDto(Vote vote){

        VoteDto voteDto = new VoteDto();
        voteDto.setId(vote.getId());
        voteDto.setVotedAt(vote.getVotedAt());
        voteDto.setOptionId(vote.getOption().getId());
        return voteDto;

    }

}
