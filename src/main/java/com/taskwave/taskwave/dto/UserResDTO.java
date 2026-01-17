package com.taskwave.taskwave.dto;

import com.taskwave.taskwave.entity.Role;
import jakarta.persistence.PrePersist;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserResDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean is_superuser;
    private Boolean is_staff;
    private Boolean is_active;
    private String position;
    private String imgProfile;
    private LocalDateTime dateJoined;
    private Set<String> roles;
}
