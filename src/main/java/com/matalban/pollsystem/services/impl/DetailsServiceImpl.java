package com.matalban.pollsystem.services.impl;

import com.matalban.pollsystem.api.v0.dto.PollDetails;
import com.matalban.pollsystem.api.v0.mappers.DetailsMapper;
import com.matalban.pollsystem.domain.Poll;
import com.matalban.pollsystem.repositories.PollRepository;
import com.matalban.pollsystem.services.DetailsService;

import org.springframework.stereotype.Service;


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
        System.out.println("getPollDetails");
        Poll poll = pollRepository.findById(id).orElseThrow(()-> new RuntimeException( "Poll id not found"));
        return detailsMapper.getDetailsFromPoll(poll);
    }
}
