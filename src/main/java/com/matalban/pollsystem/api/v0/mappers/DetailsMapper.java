package com.matalban.pollsystem.api.v0.mappers;

import com.matalban.pollsystem.api.v0.dto.PollDetails;
import com.matalban.pollsystem.api.v0.dto.WinnerOption;
import com.matalban.pollsystem.domain.Poll;
import org.springframework.stereotype.Controller;

import java.util.stream.Collectors;


@Controller
public class DetailsMapper {

    OptionMapper optionMapper;

    public DetailsMapper(OptionMapper optionMapper) {
        this.optionMapper = optionMapper;
    }

    public PollDetails getDetailsFromPoll(Poll poll){
        if (poll == null) {return null;}
        PollDetails pollDetails = new PollDetails();
        pollDetails.setId(poll.getId());
        pollDetails.setOwner(poll.getOwner().getUsername());
        pollDetails.setStatus(poll.getStatus());
        pollDetails.setExpiresAt(poll.getExpirationDate());
        pollDetails.setOptions(poll.getOptions().stream().map(optionMapper::optionToOptionDto).collect(Collectors.toList()));

        WinnerOption winnerOption = new WinnerOption();
        poll.getOptions().forEach(
                (option) -> {if(option.getWinner()){
                    winnerOption.setPollId(option.getPoll().getId());
                    winnerOption.setOptionId(option.getId());
                    }
                }
        );
        return  pollDetails;
    }
}
