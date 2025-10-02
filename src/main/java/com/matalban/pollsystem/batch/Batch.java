package com.matalban.pollsystem.batch;

import com.matalban.pollsystem.domain.Option;
import com.matalban.pollsystem.domain.Poll;
import com.matalban.pollsystem.domain.Status;
import com.matalban.pollsystem.domain.Vote;
import com.matalban.pollsystem.repositories.OptionRepository;
import com.matalban.pollsystem.repositories.PollRepository;
import com.matalban.pollsystem.repositories.VoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class Batch {

    private final PollRepository pollRepository;
    private final VoteRepository voteRepository;
    private final OptionRepository optionRepository;

    public Batch(PollRepository pollRepository , VoteRepository voteRepository,  OptionRepository optionRepository) {
        this.pollRepository = pollRepository;
        this.voteRepository = voteRepository;
        this.optionRepository = optionRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkExpirations() {
        //Get all polls
        log.info("Checking expired Polls");
         List<Poll> polls = pollRepository.findByStatus(Status.ACTIVE);

         boolean edited = false;
         //Set the status = EXPIRED and the winner if expDate < now
         for (Poll poll : polls) {
             if(poll.getExpirationDate().before(new Date())){
                poll.setStatus(Status.EXPIRED);
                Option option = getWinner(poll);
                if (option == null) {
                    log.warn("No option voted in the poll");
                    continue;
                }
                option.setWinner(true);
                optionRepository.save(option);
                log.info("Option winner:{} ",  option.getOptionName());
                log.info("Poll{} expired, winner option: {}with id:{}", poll.getId(), option.getOptionName(), option.getId());
                edited = true;
             }
         }
         if (edited) {
             pollRepository.saveAll(polls);
         }



    }



    public Option getWinner(Poll poll) {
        log.info("Getting winner for poll: {}", poll.getId());

        List<Vote> votes = voteRepository.findAllByOption_Poll_Id(poll.getId());
        if (votes.isEmpty() || poll.getTotalVote() == 0) {
            return null;
        }

        // Count votes per option
        Map<Option, Long> votesCountByOption = votes.stream()
                .collect(Collectors.groupingBy(Vote::getOption, Collectors.counting()));

        // Find the option with the max votes
        return votesCountByOption.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> {
                    Option winner = entry.getKey();
                    double percentage = entry.getValue() / (double) poll.getTotalVote();
                    winner.setPercentage(percentage*100);
                    log.info("Winner option {} has {} votes ({}%)",
                            winner.getId(), entry.getValue(), percentage * 100);
                    return winner;
                })
                .orElse(null);
    }

}
