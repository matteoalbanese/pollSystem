package com.matalban.pollsystem.services.impl;

import com.matalban.pollsystem.api.v0.dto.OptionDto;
import com.matalban.pollsystem.api.v0.dto.VoteDto;
import com.matalban.pollsystem.api.v0.mappers.OptionMapper;
import com.matalban.pollsystem.api.v0.mappers.VoteMapper;
import com.matalban.pollsystem.domain.Option;
import com.matalban.pollsystem.domain.Poll;
import com.matalban.pollsystem.domain.UserAccount;
import com.matalban.pollsystem.domain.Vote;
import com.matalban.pollsystem.repositories.OptionRepository;
import com.matalban.pollsystem.repositories.PollRepository;
import com.matalban.pollsystem.repositories.VoteRepository;
import com.matalban.pollsystem.services.OptionService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

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

        validatePollOption(pollId, optionId);

        //check the user is the owner
        if (!pollRepository.findById(pollId)
                .orElseThrow()
                .getOwner()
                .getUsername()
                .equals(getLoggedUserAccount().getUsername()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        optionRepository.deleteById(optionId);
    }

    @Override
    public OptionDto updateOption(Integer pollId, Integer optionId, OptionDto optionDto) {


        validatePollOption(pollId, optionId);

        //controllare che non ci siano voti per quel poll
        //un metodo oppure un attributo da modificare per poll isVoted()
        //valido e creo l'entità option

        Option option = optionMapper.optionDtoToOption(optionDto);
        Poll poll = pollRepository.findById(pollId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll with id " + pollId.toString() + " not found"));
        option.setPoll(poll);
        optionRepository.save(option);

        return  optionMapper.optionToOptionDto(option);
    }

    @Override
    public OptionDto vote(Integer pollId, Integer optionId) {

        validatePollOption(pollId, optionId);
        UserAccount userAccount = getLoggedUserAccount();
        //check he l'utente non sia il proprietario del poll
        if (pollRepository.findById(pollId)
                .orElseThrow()
                .getOwner()
                .getUsername()
                .equals(userAccount.getUsername()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);

        //creazione di un entità vote e salvataggio nella repo corretta

        Option option = optionRepository.findById(optionId).get();
        Vote vote = new Vote();
        vote.setUser(userAccount);
        vote.setOption(option);
        vote.setVotedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        voteRepository.save(vote);
        return optionMapper.optionToOptionDto(option);

    }

    @Override
    public VoteDto getVote(Integer pollId) {

        return voteMapper.voteToVoteDto(voteRepository.findByOption_Poll_IdAndUser_Id(pollId, getLoggedUserAccount().getId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND)));


    }

    @Override
    public OptionDto getOption(Integer pollId, Integer optionId) {

        validatePollOption(pollId, optionId);
        return optionMapper.optionToOptionDto(optionRepository.findByPollIdAndId(pollId, optionId).get());

    }


    public UserAccount getLoggedUserAccount() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return (UserAccount) authentication.getPrincipal();

    }

    public void validatePollOption(Integer pollId, Integer optionId) {

        //controllo che poll e option esistano
        if (!pollRepository.existsById(pollId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll with id " + pollId + " not found");
        if (!optionRepository.existsById(optionId))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Option with id "+ optionId + " not found");

    }
}
