package com.taskwave.taskwave.service;


import com.taskwave.taskwave.dto.RegisterReqDTO;
import com.taskwave.taskwave.entity.Register;
import com.taskwave.taskwave.repository.RegisterRepository;
import com.taskwave.taskwave.repository.UserRepository;
import com.taskwave.taskwave.exception.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private  final RegisterRepository registerRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       RegisterRepository registerRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        registerRepository.save(user);
    }
}