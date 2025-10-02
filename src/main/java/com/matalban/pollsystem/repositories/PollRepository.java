package com.matalban.pollsystem.repositories;


import com.matalban.pollsystem.domain.Poll;
import com.matalban.pollsystem.domain.Status;
import io.micrometer.common.lang.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PollRepository extends JpaRepository<Poll, Integer> {

    Page<Poll> findAll(@NonNull Pageable pageable);

    List<Poll> findByStatus(Status status);
}
