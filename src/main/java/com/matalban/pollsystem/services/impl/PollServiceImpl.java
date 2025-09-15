package com.matalban.pollsystem.services.impl;

import com.matalban.pollsystem.api.v0.dto.PollDto;
import com.matalban.pollsystem.api.v0.mappers.PollMapper;
import com.matalban.pollsystem.domain.Poll;
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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }


        //Dto username match the user logged in
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        System.out.println("user logged "+ userAccount.getUsername());
        if (userAccount.getUsername().isEmpty() || !userAccount.getUsername().equals(pollDto.getOwner())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
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
        Poll poll = pollRepository.findById(pollId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return pollMapper.pollToPollDto(poll);

    }

    @Override
    public void deletePoll(Integer pollId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Poll poll = pollRepository.findById(pollId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
        if (poll.getOwner().getUsername().equals(userAccount.getUsername()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        pollRepository.delete(poll);
    }

    @Override
    public PollDto updatePoll(Integer id,PollDto pollDto) {

        if (pollRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        //controlli validità pollDto

        //persistenza delle modifiche

        Poll pollSaved = pollMapper.pollDtoToPoll(pollDto);
        pollRepository.save(pollSaved);

        return pollMapper.pollToPollDto(pollSaved);
    }
}
