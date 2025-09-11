package com.matalban.pollsystem.repositories;


import com.matalban.pollsystem.domain.Poll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PollRepository extends JpaRepository<Poll, Integer> {
}
