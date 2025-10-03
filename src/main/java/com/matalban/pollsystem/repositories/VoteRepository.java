package com.matalban.pollsystem.repositories;


import com.matalban.pollsystem.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Integer> {

    Optional<Vote> findByOption_Poll_IdAndUser_Id(Integer pollId, Integer userId);
    List<Vote> findByUser_Id(Integer userId);
    List<Vote> findAllByOption_Poll_Id( Integer pollId);
    Boolean existsByOption_Id(Integer optionId);
}
