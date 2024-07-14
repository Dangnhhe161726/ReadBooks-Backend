package com.example.backend.services.user;


import com.example.backend.models.dtos.LoginDTO;
import com.example.backend.models.dtos.RegisterDTO;
import com.example.backend.models.entities.UserEntity;
import com.example.backend.models.responses.UserTokenResponse;

public interface IAuthService {

    String login (LoginDTO loginDto);
    UserEntity register(RegisterDTO registerDto);
    Boolean verifyToken(String token) throws Exception;
    UserTokenResponse loadUserByUsername(String username) throws Exception;
}
