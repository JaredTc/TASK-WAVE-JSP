package com.taskwave.taskwave.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "task_wave_customuser")
public class Register {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private Boolean is_superuser;
    private Boolean is_staff;
    private Boolean is_active;

    private String position;

    @Column(name = "img_profile")
    private String imgProfile;

    @Column(name = "date_joined", nullable = false)
    private LocalDateTime dateJoined;

    // 👇 ESTE MÉTODO SE EJECUTA ANTES DEL INSERT
    @PrePersist
    public void prePersist() {
        this.dateJoined = LocalDateTime.now();
    }

}
