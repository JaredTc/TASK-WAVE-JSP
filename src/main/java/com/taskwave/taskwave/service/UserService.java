package com.taskwave.taskwave.service;


import com.taskwave.taskwave.dto.RegisterReqDTO;
import com.taskwave.taskwave.dto.TasksDTO;
import com.taskwave.taskwave.dto.UserReqDTO;
import com.taskwave.taskwave.dto.UserResDTO;
import com.taskwave.taskwave.entity.Users;
import com.taskwave.taskwave.entity.Role;
import com.taskwave.taskwave.exception.ResourceNotFoundException;
import com.taskwave.taskwave.repository.RegisterRepository;
import com.taskwave.taskwave.repository.RoleRepository;
import com.taskwave.taskwave.repository.AuthRepository;
import com.taskwave.taskwave.exception.BadRequestException;
import com.taskwave.taskwave.repository.UsersRepository;
import com.taskwave.taskwave.util.Helper;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final AuthRepository authRepository;
    private final RegisterRepository registerRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final UsersRepository usersRepository;

    private final Helper helper;

    public UserService(
            AuthRepository authRepository,
            RegisterRepository registerRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService,
            UsersRepository usersRepository,
            Helper helper) {

        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.registerRepository = registerRepository;
        this.emailService = emailService;
        this.usersRepository = usersRepository;
        this.helper = helper;
    }

    public void register(RegisterReqDTO dto) {

        if (authRepository.findByUsername(dto.username).isPresent()) {
            throw new BadRequestException("El usuario ya existe");
        }
        Users user = new Users();
        user.setUsername(dto.username);
        user.setEmail(dto.email);
        user.setFirstName(dto.firstName);
        user.setLastName(dto.lastName);
        user.setPosition(dto.position);
        user.setImgProfile(dto.imgProfile);
        user.setPassword(passwordEncoder.encode(dto.password));
        user.setIs_active(true);
        user.setIs_staff(false);
        user.setIs_superuser(false);

        // 2️⃣ Asignar roles
        if (dto.roles != null && !dto.roles.isEmpty()) {
            Set<Role> roles = dto.roles.stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleName)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        } else {
            // rol default si no envían roles
            Role defaultRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Rol USER no existe"));
            user.setRoles(Set.of(defaultRole));
        }
        emailService.sendWelcomeEmail(
                user.getEmail(),
                user.getFirstName()
        );
        registerRepository.save(user);
    }

    @Transactional
    public UserResDTO updateUser(Long id, UserReqDTO dto) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setFirstName(dto.firstName);
        user.setLastName(dto.lastName);
        user.setPosition(dto.position);
        user.setImgProfile(dto.imgProfile);
        // Actualizar otros campos según sea necesario

        Users updatedUser = usersRepository.save(user);
        return helper.mapToDto(updatedUser);

    }


    @Transactional
    public void deleteUser(Long id) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        usersRepository.delete(user);
    }


    public List<UserResDTO> listUsers() {
        return usersRepository.findAll()
                .stream()
                .map(helper::mapToDto)
                .toList();
    }

    public UserResDTO profileUser(Users currentUser) {
        Users user = usersRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return helper.mapToDto(user);


    }
}