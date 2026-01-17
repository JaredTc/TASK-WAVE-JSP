package com.taskwave.taskwave.repository;

import com.taskwave.taskwave.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository  extends JpaRepository<Users, Long> {
}
