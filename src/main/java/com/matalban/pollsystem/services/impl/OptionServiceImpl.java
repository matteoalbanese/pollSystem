package com.matalban.pollsystem.services.impl;

import com.matalban.pollsystem.api.v0.dto.OptionDto;
import com.matalban.pollsystem.api.v0.dto.VoteDto;
import com.matalban.pollsystem.api.v0.mappers.OptionMapper;
import com.matalban.pollsystem.api.v0.mappers.VoteMapper;
import com.matalban.pollsystem.domain.*;
import com.matalban.pollsystem.repositories.OptionRepository;
import com.matalban.pollsystem.repositories.PollRepository;
import com.matalban.pollsystem.repositories.VoteRepository;
import com.matalban.pollsystem.services.OptionService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class OptionServiceImpl implements OptionService {

    private final PollRepository pollRepository;
    private final OptionRepository optionRepository;
    private final VoteRepository voteRepository;
    private final OptionMapper optionMapper;
    private final VoteMapper voteMapper;

    public OptionServiceImpl(PollRepository pollRepository, OptionRepository optionRepository, VoteRepository voteRepository, OptionMapper optionMapper,  VoteMapper voteMapper) {
        this.pollRepository = pollRepository;
        this.optionRepository = optionRepository;
        this.voteRepository = voteRepository;
        this.optionMapper = optionMapper;
        this.voteMapper = voteMapper;
    }

    @Override
    public OptionDto insertOption(Integer pollId, OptionDto optionDto) {

        if (!pollRepository.findById(pollId)
                .orElseThrow()
                .getOwner()
                .getUsername()
                .equals(getLoggedUserAccount().getUsername()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not the owner of the poll");

        Option option = optionMapper.optionDtoToOption(optionDto);

        option.setPoll(
                pollRepository.findById(pollId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll with id " + pollId + " not found"))
        );

        optionRepository.save(option);

        return  optionMapper.optionToOptionDto(option);
    }

    @Override
    public void deleteOption(Integer pollId, Integer optionId) {


        //check the user is the owner
        if (!pollRepository.findById(pollId)
                .orElseThrow()
                .getOwner()
                .getUsername()
                .equals(getLoggedUserAccount().getUsername()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not the owner of the poll");
        if (!optionRepository.existsById(optionId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Option with id "+ optionId + " not found");

        optionRepository.deleteById(optionId);
    }

    @Override
    public OptionDto updateOption(Integer pollId, Integer optionId, OptionDto optionDto) {


        Poll poll = pollRepository.findById(pollId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll with id " + pollId+ " not found"));
        if(poll.getStatus().equals(Status.EXPIRED)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Poll expired");
        }
        Option option = optionRepository.findById(optionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Option with id " + optionId + " not found"));
        option.setOptionName(optionDto.getMessage());
        option.setCreatedAt(new Date());
        optionRepository.save(option);

        return  optionMapper.optionToOptionDto(option);
    }

    @Override
    @Transactional
    public OptionDto vote(Integer pollId, Integer optionId) {

        UserAccount userAccount = getLoggedUserAccount();
        //check he l'utente non sia il proprietario del poll
        Poll poll = pollRepository.findById(pollId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll with id " + pollId + " not found"));

        if(poll.getStatus().equals(Status.EXPIRED))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Poll expired");


        //il proprietario non può votare
        if (poll.getOwner()
                .getUsername()
                .equals(userAccount.getUsername()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are the owner of the poll");



        Option option = optionRepository.findByPollIdAndId(pollId,optionId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Option with id " + optionId + " not found"));

        //creazione di un entità vote e salvataggio nella repo corretta

        //controllare che non abbia già votato
//        if (voteRepository.existsByUser_Id(userAccount.getId())){
//            Vote vote = voteRepository.findAllByOption_Poll_Id().orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unexpected error"));
//            vote.setOption(option);
//            voteRepository.save(vote);
//            return optionMapper.optionToOptionDto(option);
//
//
//        }

        //Se l'utente ha già votato il poll allora cambio la option del suo voto e return new voted option
        List<Vote> existingVotes = voteRepository.findByUser_Id(userAccount.getId());

        Optional<Vote> existingVoteForPoll = existingVotes.stream()
                .filter(v -> v.getOption().getPoll().getId().equals(poll.getId()))
                .findFirst(); // findFirst returns Optional<Vote>

        if (existingVoteForPoll.isPresent()){
            existingVoteForPoll.get().setOption(option);
            voteRepository.save(existingVoteForPoll.get());
            return  optionMapper.optionToOptionDto(option);
        }


        //l'utente non ha mai votato allora creo il voto
        Vote vote = new Vote();
        vote.setUser(userAccount);
        vote.setOption(option);
        vote.setVotedAt(new Date());
        voteRepository.save(vote);
        poll.setTotalVote(poll.getTotalVote() + 1);
        pollRepository.save(poll);



        return optionMapper.optionToOptionDto(option);

    }

    @Override
    public VoteDto getVote(Integer pollId) {

        return voteMapper.voteToVoteDto(voteRepository.findByOption_Poll_IdAndUser_Id(pollId, getLoggedUserAccount().getId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll or option not found ")));


    }

    @Override
    public OptionDto getOption(Integer pollId, Integer optionId) {

        if (!pollRepository.existsById(pollId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll with id " + pollId + " not found");


        return optionMapper.optionToOptionDto(
                optionRepository.findByPollIdAndId(pollId, optionId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Option with id " + optionId + " not found")));

    }


    public UserAccount getLoggedUserAccount() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not the owner of the poll");
        }

        return (UserAccount) authentication.getPrincipal();

    }


}
