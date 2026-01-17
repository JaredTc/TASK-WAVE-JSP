package com.taskwave.taskwave.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // ejemplo: "ADMIN", "MANAGER", "USER"

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}