package com.taskwave.taskwave.repository;

import com.taskwave.taskwave.entity.Register;
import com.taskwave.taskwave.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

}