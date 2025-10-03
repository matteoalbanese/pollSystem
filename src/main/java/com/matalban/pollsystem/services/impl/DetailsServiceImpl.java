package com.matalban.pollsystem.services.impl;

import com.matalban.pollsystem.api.v0.dto.PollDetails;
import com.matalban.pollsystem.api.v0.dto.WinnerOption;
import com.matalban.pollsystem.api.v0.mappers.DetailsMapper;
import com.matalban.pollsystem.domain.Poll;
import com.matalban.pollsystem.repositories.PollRepository;
import com.matalban.pollsystem.services.DetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class DetailsServiceImpl implements DetailsService {

    private final DetailsMapper detailsMapper;
    private final PollRepository pollRepository;


    public DetailsServiceImpl(DetailsMapper detailsMapper , PollRepository pollRepository) {
        this.detailsMapper = detailsMapper;
        this.pollRepository = pollRepository;

    }

    @Override
    public PollDetails getPollDetails(Integer id) {
        //recupera il poll dalla repo
        //mappa i campi nell'oggetto poll Details
        log.info("pollDetails started");
        Poll poll = pollRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Poll with id"+id+" not found"));
        PollDetails pollDetails = detailsMapper.getDetailsFromPoll(poll);
        poll.getOptions().stream()
                .filter(o -> Boolean.TRUE.equals(o.getWinner()))
                .findFirst()
                .ifPresent(o -> {
                    WinnerOption winner = new WinnerOption();
                    winner.setPollId(poll.getId());
                    winner.setOptionId(o.getId());
                    winner.setPercentOfWinner(o.getPercentage());
                    pollDetails.setWinnerOption(winner);
                });

        return pollDetails;
    }
}
