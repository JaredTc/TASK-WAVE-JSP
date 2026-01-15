package com.taskwave.taskwave.repository;

import com.taskwave.taskwave.entity.Tasks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskReposirtory extends JpaRepository<Tasks, Long> {

    Page<Tasks> findByTitleContainingIgnoreCaseOrStatusIgnoreCase(
            String title,
            String status,
            Pageable pageable
    );
}
