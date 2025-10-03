package com.matalban.pollsystem.services.impl;

import com.matalban.pollsystem.api.v0.dto.PollDto;
import com.matalban.pollsystem.api.v0.mappers.PollMapper;
import com.matalban.pollsystem.domain.Poll;
import com.matalban.pollsystem.domain.Status;
import com.matalban.pollsystem.domain.UserAccount;
import com.matalban.pollsystem.repositories.PollRepository;
import com.matalban.pollsystem.services.PollService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
public class PollServiceImpl implements PollService {



    private final PollRepository pollRepository;
    private final PollMapper pollMapper;


    public PollServiceImpl(PollRepository pollRepository,  PollMapper pollMapper) {
        this.pollRepository = pollRepository;
        this.pollMapper = pollMapper;
    }

    @Override
    public PollDto createPoll(PollDto pollDto) {

        log.info("pollCreate started");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not logged in");
        }

        //Dto username match the user logged in
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        if (userAccount.getUsername().isEmpty() || !userAccount.getUsername().equals(pollDto.getOwner())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid poll owner");
        }

        Poll savedPoll = pollMapper.pollDtoToPoll(pollDto);

        savedPoll.setOwner(userAccount);

        savedPoll = pollRepository.save(savedPoll);

        return pollMapper.pollToPollDto(savedPoll);

    }

    @Override
    public Page <PollDto> getPollListPage(Integer pageParam, Integer sizeParam) {

        log.info("getPollListPage started");
        Pageable params  = PageRequest.of(pageParam, sizeParam);
        Page<Poll> page=  pollRepository.findAll(params);
        return page.map(pollMapper::pollToPollDto);
    }

    @Override
    public PollDto getPoll(Integer pollId) {
        log.info("getPoll started");
        Poll poll = pollRepository.findById(pollId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll with id " + pollId + " not found"));
        return pollMapper.pollToPollDto(poll);

    }

    @Override
    public void deletePoll(Integer pollId) {

        log.info("deletePoll started");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not logged in");
        }
        Poll poll = pollRepository.findById(pollId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll with id" + pollId + " not found"));
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        if (!poll.getOwner().getUsername().equals(userAccount.getUsername()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not the owner of this poll");

        pollRepository.delete(poll);
    }

    @Override
    public PollDto updatePoll(Integer id,PollDto pollDto) {

        log.info("updatePoll started");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Poll existingPoll = pollRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll with id" + id + " not found"));

        if (existingPoll.getStatus().equals(Status.EXPIRED))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Poll has expired");
        if(existingPoll.getTotalVote() != 0)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Some options in the poll have been voted, the poll cannot be updated");

        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        if (!existingPoll.getOwner().getUsername().equals(userAccount.getUsername()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not the owner of this poll");

        existingPoll.setQuestion(pollDto.getQuestion());
        existingPoll.setExpirationDate(pollDto.getExpiresAt());

        pollRepository.save(existingPoll);

        return pollMapper.pollToPollDto(existingPoll);
    }
}
