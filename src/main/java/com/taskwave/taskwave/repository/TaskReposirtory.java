package com.taskwave.taskwave.repository;

import com.taskwave.taskwave.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskReposirtory extends JpaRepository<Tasks, Long> {
}
