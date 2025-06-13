package com.example.projectservice.repo;

import com.example.projectservice.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepo extends JpaRepository<UserAccount, Integer> {

    java.util.Optional<UserAccount> findByUsername(String username);
}
