package com.matalban.pollsystem.repositories;


import com.matalban.pollsystem.domain.Poll;
import io.micrometer.common.lang.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;



public interface PollRepository extends JpaRepository<Poll, Integer> {

    Page<Poll> findAll(@NonNull Pageable pageable);
}
