package com.taskwave.taskwave.service;


import com.taskwave.taskwave.dto.RegisterReqDTO;
import com.taskwave.taskwave.entity.Register;
import com.taskwave.taskwave.entity.Role;
import com.taskwave.taskwave.repository.RegisterRepository;
import com.taskwave.taskwave.repository.RoleRepository;
import com.taskwave.taskwave.repository.UserRepository;
import com.taskwave.taskwave.exception.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private  final RegisterRepository registerRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository,
                       RegisterRepository registerRepository,
                          RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.registerRepository = registerRepository;
    }

    public void register(RegisterReqDTO dto) {

        if (userRepository.findByUsername(dto.username).isPresent()) {
            throw new BadRequestException("El usuario ya existe");
        }

        Register user = new Register();
        user.setUsername(dto.username);
        user.setEmail(dto.email);
        user.setFirstName(dto.firstName);
        user.setLastName(dto.lastName);
        user.setPosition(dto.position);
        user.setImgProfile(dto.imgProfile);



        //  ENCRIPTAR PASSWORD (BCrypt)
        user.setPassword(
                passwordEncoder.encode(dto.password)
        );

        // valores por defecto
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

         registerRepository.save(user);
    }
}