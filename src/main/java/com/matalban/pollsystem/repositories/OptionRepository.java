package com.matalban.pollsystem.repositories;

import com.matalban.pollsystem.domain.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OptionRepository extends JpaRepository<Option, Integer> {

    Optional<Option> findByPollIdAndId(Integer pollId, Integer optionId);
}
