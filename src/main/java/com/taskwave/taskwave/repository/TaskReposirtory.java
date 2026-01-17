package com.taskwave.taskwave.repository;

import com.taskwave.taskwave.entity.Tasks;
import com.taskwave.taskwave.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskReposirtory extends JpaRepository<Tasks, Long> {

    Page<Tasks> findByTitleContainingIgnoreCaseOrStatusIgnoreCase(
            String title,
            String status,
            Pageable pageable
    );
    List<Tasks> findByAssignedTo(User user);
}
