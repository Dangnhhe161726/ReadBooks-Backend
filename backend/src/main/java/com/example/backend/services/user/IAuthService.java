package com.example.backend.services.user;


import com.example.backend.models.dtos.ChangePasswordDTO;
import com.example.backend.models.dtos.LoginDTO;
import com.example.backend.models.dtos.RegisterDTO;
import com.example.backend.models.dtos.UpdateProfileDTO;
import com.example.backend.models.entities.UserEntity;

import jakarta.validation.Valid;

public interface IAuthService {

    String login (LoginDTO loginDto);
    UserEntity register(RegisterDTO registerDto);
    Boolean verifyToken(String token) throws Exception;
    UserEntity getUserProfile(String userEmail);
    void changePassword(String userEmail, @Valid ChangePasswordDTO changePasswordDto);
    void updateProfile(String userEmail, @Valid UpdateProfileDTO updateProfileDTO);
    
}
