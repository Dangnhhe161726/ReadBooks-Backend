package com.example.backend.controllers;
import com.example.backend.configs.jwt.JwtGenerator;
import com.example.backend.models.dtos.ChangePasswordDTO;
import com.example.backend.models.dtos.LoginDTO;
import com.example.backend.models.dtos.RegisterDTO;
import com.example.backend.models.dtos.UpdateProfileDTO;
import com.example.backend.models.entities.UserEntity;
import com.example.backend.models.responses.HttpResponse;
import com.example.backend.models.responses.UserResponse;
import com.example.backend.models.responses.UserTokenResponse;
import com.example.backend.services.user.IAuthService;
import com.example.backend.validations.ValidationDataRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;
    private final ModelMapper modelMapper;
    private final JwtGenerator jwtGenerator;
    private String timeStamp = LocalDateTime.now().toString();

        @PostMapping("/login")
        public ResponseEntity<HttpResponse> login(
                        @Valid @RequestBody LoginDTO loginDto,
                        BindingResult result) {
                if (result.hasErrors()) {
                        return ResponseEntity.badRequest().body(
                                        HttpResponse.builder()
                                                        .timeStamp(timeStamp)
                                                        .message(ValidationDataRequest.getMessageError(result))
                                                        .statusCode(HttpStatus.BAD_REQUEST.value())
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build());
                }

                String token = authService.login(loginDto);
                return ResponseEntity.ok().body(
                                HttpResponse.builder()
                                                .timeStamp(timeStamp)
                                                .status(HttpStatus.OK)
                                                .statusCode(HttpStatus.OK.value())
                                                .message("Login successfull")
                                                .data(Map.of("Token", token))
                                                .build());
        }

        @PostMapping("/register")
        public ResponseEntity<HttpResponse> register(
                        @Valid @RequestBody RegisterDTO registerDto,
                        BindingResult result) {
                try {
                        if (result.hasErrors()) {
                                throw new InvalidParameterException(
                                                ValidationDataRequest.getMessageError(result));
                        }

                        if (!registerDto.getPassword().equalsIgnoreCase(registerDto.getRepassword())) {
                                throw new InvalidParameterException("New password not equal with repassword");
                        }

                        UserEntity newUserEntity = authService.register(registerDto);
                        return ResponseEntity.created(URI.create("")).body(
                                        HttpResponse.builder()
                                                        .timeStamp(timeStamp)
                                                        .status(HttpStatus.CREATED)
                                                        .statusCode(HttpStatus.CREATED.value())
                                                        .message("User created")
                                                        .data(Map.of("user",
                                                                        modelMapper.map(newUserEntity,
                                                                                        UserResponse.class)))
                                                        .build());
                } catch (Exception e) {
                        return ResponseEntity.badRequest().body(
                                        HttpResponse.builder()
                                                        .statusCode(HttpStatus.BAD_REQUEST.value())
                                                        .timeStamp(timeStamp)
                                                        .message(e.getMessage())
                                                        .build());
                }

        }

        @GetMapping("")
        public ResponseEntity<HttpResponse> confirmUserAccount(@RequestParam("token") String token) {
                try {
                        Boolean isSuccess = authService.verifyToken(token);
                        return ResponseEntity.ok().body(
                                        HttpResponse.builder()
                                                        .timeStamp(timeStamp)
                                                        .status(HttpStatus.OK)
                                                        .statusCode(HttpStatus.OK.value())
                                                        .message("Verify success")
                                                        .data(Map.of("Success", isSuccess))
                                                        .build());
                } catch (Exception e) {
                        return ResponseEntity.badRequest().body(
                                        HttpResponse.builder()
                                                        .timeStamp(timeStamp)
                                                        .message(e.getMessage())
                                                        .build());
                }
        }
        @GetMapping("/userinfo")
        public ResponseEntity<HttpResponse> getUserInfo(Authentication authentication) {
            try {
    
                String username = authentication.getName();
                UserTokenResponse user = authService.loadUserByUsername(username);
                return ResponseEntity.ok().body(
                        HttpResponse.builder()
                                .timeStamp(timeStamp)
                                .status(HttpStatus.OK)
                                .message("Get user success")
                                .data(Map.of("userByToken", user))
                                .build()
                );
            } catch (Exception e) {
                // Handle exceptions
                return ResponseEntity.badRequest().body(
                        HttpResponse.builder()
                                .timeStamp(timeStamp)
                                .message(e.getMessage())
                                .build()
                );
            }
        }
        @GetMapping("/profile")
        public ResponseEntity<HttpResponse> getProfile(Authentication authentication) {
                try {
                        // Lấy thông tin người dùng đã xác thực từ đối tượng Authentication
                        String userEmail = authentication.getName(); // Lấy email của người dùng đã xác thực
                        UserEntity userEntity = authService.getUserProfile(userEmail);

                        // Map UserEntity sang UserResponse
                        UserResponse userResponse = modelMapper.map(userEntity, UserResponse.class);

                        // Trả về response chứa thông tin hồ sơ người dùng
                        return ResponseEntity.ok().body(
                                        HttpResponse.builder()
                                                        .timeStamp(LocalDateTime.now().toString())
                                                        .status(HttpStatus.OK)
                                                        .statusCode(HttpStatus.OK.value())
                                                        .message("Profile fetched successfully")
                                                        .data(Map.of("user", userResponse))
                                                        .build());
                } catch (Exception e) {
                        // Xử lý các ngoại lệ và trả về response lỗi
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                                        HttpResponse.builder()
                                                        .timeStamp(LocalDateTime.now().toString())
                                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                                        .message(e.getMessage())
                                                        .build());
                }
        }

        @PostMapping("/change-password")
        public ResponseEntity<HttpResponse> changePassword(
                        @Valid @RequestBody ChangePasswordDTO changePasswordDto,
                        BindingResult result,
                        Authentication authentication) {
                if (result.hasErrors()) {
                        return ResponseEntity.badRequest().body(
                                        HttpResponse.builder()
                                                        .timeStamp(LocalDateTime.now().toString())
                                                        .message(ValidationDataRequest.getMessageError(result))
                                                        .statusCode(HttpStatus.BAD_REQUEST.value())
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build());
                }

                try {
                        String userEmail = authentication.getName();
                        authService.changePassword(userEmail, changePasswordDto);

                        return ResponseEntity.ok().body(
                                        HttpResponse.builder()
                                                        .timeStamp(LocalDateTime.now().toString())
                                                        .status(HttpStatus.OK)
                                                        .statusCode(HttpStatus.OK.value())
                                                        .message("Password changed successfully")
                                                        .build());
                } catch (Exception e) {
                        return ResponseEntity.badRequest().body(
                                        HttpResponse.builder()
                                                        .statusCode(HttpStatus.BAD_REQUEST.value())
                                                        .timeStamp(LocalDateTime.now().toString())
                                                        .message(e.getMessage())
                                                        .build());
                }
        }

        @PostMapping("/update-profile")
        public ResponseEntity<HttpResponse> updateProfile(
                        @Valid @RequestBody UpdateProfileDTO updateProfileDTO,
                        BindingResult result,
                        Authentication authentication) {
                if (result.hasErrors()) {
                        return ResponseEntity.badRequest().body(
                                        HttpResponse.builder()
                                                        .timeStamp(timeStamp)
                                                        .message(ValidationDataRequest.getMessageError(result))
                                                        .statusCode(HttpStatus.BAD_REQUEST.value())
                                                        .status(HttpStatus.BAD_REQUEST)
                                                        .build());
                }

                try {
                        String userEmail = authentication.getName(); // Lấy email của người dùng đã xác thực
                        authService.updateProfile(userEmail, updateProfileDTO);

                        // Lấy thông tin hồ sơ sau khi cập nhật
                        UserEntity updatedUser = authService.getUserProfile(userEmail);
                        UserResponse userResponse = modelMapper.map(updatedUser, UserResponse.class);

                        return ResponseEntity.ok().body(
                                        HttpResponse.builder()
                                                        .timeStamp(LocalDateTime.now().toString())
                                                        .status(HttpStatus.OK)
                                                        .statusCode(HttpStatus.OK.value())
                                                        .message("Profile updated successfully")
                                                        .data(Map.of("user", userResponse))
                                                        .build());
                } catch (Exception e) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                                        HttpResponse.builder()
                                                        .timeStamp(LocalDateTime.now().toString())
                                                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                                        .message(e.getMessage())
                                                        .build());
                }
        }

}
