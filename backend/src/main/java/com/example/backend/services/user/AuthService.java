package com.example.backend.services.user;


import com.example.backend.configs.jwt.JwtGenerator;
import com.example.backend.models.dtos.LoginDTO;
import com.example.backend.models.dtos.RegisterDTO;
import com.example.backend.models.entities.Confirmation;
import com.example.backend.models.entities.Role;
import com.example.backend.models.entities.UserEntity;
import com.example.backend.models.responses.BookResponse;
import com.example.backend.models.responses.UserTokenResponse;
import com.example.backend.repositories.ConfirmationRepository;
import com.example.backend.repositories.RoleRepository;
import com.example.backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtGenerator jwtGenerator;
    private final ConfirmationRepository confirmationRepository;
    private final IEmailService emailService;
    private final ModelMapper modelMapper;

    @Override
    public String login(LoginDTO loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow();

        if (!user.isActive()) {
            return null;
        }

        var token = jwtGenerator.generateToken(user.getEmail());

        return token;
    }

    @Override
    @Transactional
    public UserEntity register(RegisterDTO registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        List<Role> roles = new ArrayList<>();
        Optional<Role> checkRole = null;
        for (Long roleId : registerDto.getRoles()) {
            checkRole = roleRepository.findById(roleId);
            if (checkRole.isPresent()) {
                roles.add(checkRole.get());
            } else {
                throw new NoSuchElementException("Not found this role");
            }
        }
        UserEntity userEntity = modelMapper.map(registerDto, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userEntity.setActive(false);
        userEntity.setRoles(roles);
        userRepository.save(userEntity);

        var token = jwtGenerator.generateToken(userEntity.getEmail());
        Confirmation confirmation = new Confirmation(userEntity, token);
        confirmationRepository.save(confirmation);

        //Send email with token
        emailService.sendHtmlEmailWithEmbeddedFiles(userEntity.getFullName(),
                userEntity.getEmail(), token);
        return userEntity;
    }

    @Override
    @Transactional
    public Boolean verifyToken(String token) throws Exception {
        Confirmation confirmation = confirmationRepository.findByToken(token)
                .orElseThrow(() -> new NoSuchElementException("Confiamtion not found token"));
        Optional<UserEntity> userEntity = userRepository.findByEmail(
                confirmation.getUserEntity()
                        .getEmail()
        );
        if (!userEntity.isPresent()) {
            throw new RuntimeException("Email verify not exist");
        }
        userEntity.get().setActive(true);
        confirmationRepository.delete(confirmation);
        return Boolean.TRUE;
    }

    @Override
    public UserTokenResponse loadUserByUsername(String username) throws Exception {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(username);
        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();
            return modelMapper.map(userEntity, UserTokenResponse.class);
        }
        throw new Exception("User not found with email: " + username);
    }

}
