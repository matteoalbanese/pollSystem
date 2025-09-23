package com.matalban.pollsystem.services.impl;

import com.matalban.pollsystem.api.v0.dto.PollDto;
import com.matalban.pollsystem.api.v0.mappers.PollMapper;
import com.matalban.pollsystem.domain.Poll;
import com.matalban.pollsystem.domain.Status;
import com.matalban.pollsystem.domain.UserAccount;
import com.matalban.pollsystem.repositories.PollRepository;
import com.matalban.pollsystem.services.PollService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


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

        // non mi serve, ma è richiesto, fare il controllo dell'owner quando creo un poll perchè inserisco l'ownen
        // posso controllare che l'owner inserito corrisponda all'utente che sta facendo l'operazione


        System.out.println("createPoll 1 ");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not logged in");
        }

        //Dto username match the user logged in
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        if (userAccount.getUsername().isEmpty() || !userAccount.getUsername().equals(pollDto.getOwner())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid poll owner");
        }

        //dto inserted validation controls
        //if(pollDto.getExpiresAt())
          //  throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Poll savedPoll = pollMapper.pollDtoToPoll(pollDto);

        savedPoll.setOwner(userAccount);

        pollRepository.save(savedPoll);

        return pollMapper.pollToPollDto(savedPoll);

    }

    @Override
    public Page <PollDto> getPollListPage(Integer pageParam, Integer sizeParam) {

        Pageable params  = PageRequest.of(pageParam, sizeParam);
        Page<Poll> page=  pollRepository.findAll(params);
        return page.map(pollMapper::pollToPollDto);
    }

    @Override
    public PollDto getPoll(Integer pollId) {
        Poll poll = pollRepository.findById(pollId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll with id " + pollId + " not found"));
        return pollMapper.pollToPollDto(poll);

    }

    @Override
    public void deletePoll(Integer pollId) {

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Poll existingPoll = pollRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll with id" + id + " not found"));

        if(!existingPoll.getStatus().equals(Status.ACTIVE))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Some options in the poll have been voted, the poll cannot be updated");

        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        if (!existingPoll.getOwner().getUsername().equals(userAccount.getUsername()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not the owner of this poll");

        existingPoll.setQuestion(pollDto.getQuestion());
        existingPoll.setExpirationDate(pollDto.getExpiresAt());
        //controlli validità pollDto

        //persistenza delle modifiche
        pollRepository.save(existingPoll);

        return pollMapper.pollToPollDto(existingPoll);
    }
}
