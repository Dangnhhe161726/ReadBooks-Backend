package com.example.backend.services.user;

import com.example.backend.configs.jwt.JwtGenerator;
import com.example.backend.models.dtos.ChangePasswordDTO;
import com.example.backend.models.dtos.LoginDTO;
import com.example.backend.models.dtos.RegisterDTO;
import com.example.backend.models.dtos.UpdateProfileDTO;
import com.example.backend.models.entities.Confirmation;
import com.example.backend.models.entities.Role;
import com.example.backend.models.entities.Token;
import com.example.backend.models.entities.UserEntity;
import com.example.backend.models.responses.BookResponse;
import com.example.backend.models.responses.UserTokenResponse;
import com.example.backend.repositories.ConfirmationRepository;
import com.example.backend.repositories.RoleRepository;
import com.example.backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

        // Token storedToken = new Token();
        // storedToken.setToken(token);
        // storedToken.setUserEntity(user); // Set the user for the token
        // storedToken.setRevoked(false);
        // tokenRepository.save(storedToken);

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

        // Send email with token
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
                        .getEmail());
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

    public UserEntity getUserProfile(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("User not found with email: " + userEmail));
    }

    @Override
    public void changePassword(String userEmail, @Valid ChangePasswordDTO changePasswordDto) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmNewPassword())) {
            throw new RuntimeException("New password and confirm password do not match");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateProfile(String userEmail, @Valid UpdateProfileDTO updateProfileDTO) {
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new NoSuchElementException("User not found with email: " + userEmail));

        user.setFullName(updateProfileDTO.getFullName());
        user.setEmail(updateProfileDTO.getEmail());
        user.setPhoneNumber(updateProfileDTO.getPhoneNumber());

        // Chuyển đổi LocalDate từ UpdateProfileDTO sang java.sql.Date
        LocalDate localDate = LocalDate.parse(updateProfileDTO.getFormattedDob(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        user.setDob(Date.valueOf(localDate));

        user.setAddress(updateProfileDTO.getAddress());
        user.setGender(updateProfileDTO.isGender());

        userRepository.save(user);
    }

}
