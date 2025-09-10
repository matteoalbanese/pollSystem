package com.matalban.pollsystem.repositories;

import com.matalban.pollsystem.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserAccount,Long> {


    boolean existsByUsername(String username);
    Optional<UserAccount> findByUsername(String username);
}
