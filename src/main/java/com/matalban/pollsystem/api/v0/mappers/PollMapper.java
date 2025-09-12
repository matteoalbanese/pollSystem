package com.matalban.pollsystem.api.v0.mappers;

import com.matalban.pollsystem.api.v0.dto.PollDto;
import com.matalban.pollsystem.domain.Poll;
import com.matalban.pollsystem.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class PollMapper {



    private final UserRepository userRepository;

    public PollMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public PollDto pollToPollDto(Poll poll){

        if (poll == null) {return null;}
        PollDto pollDto = new PollDto();
        pollDto.setOwner(poll.getOwner().getUsername());
        pollDto.setId(poll.getId());
        pollDto.setQuestion(poll.getQuestion());
        pollDto.setStatus(poll.getStatus());
        pollDto.setExpiresAt(poll.getExpirationDate());


        return  pollDto;

    }

    public Poll pollDtoToPoll(PollDto pollDto){

        if (pollDto == null) {return null;}

        Poll poll = new Poll();
        //TODO check
        poll.setId(pollDto.getId());
        poll.setQuestion(pollDto.getQuestion());
        poll.setExpirationDate(pollDto.getExpiresAt());
        poll.setStatus(pollDto.getStatus());

        poll.setOwner(userRepository.findByUsername(pollDto.getOwner()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));

        return poll;
    }

}
