package com.taskwave.taskwave.repository;

import com.taskwave.taskwave.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Categoryrepository extends JpaRepository<Category, Long> {
}
