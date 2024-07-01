package com.example.backend.services.user;


import com.example.backend.models.dtos.LoginDTO;
import com.example.backend.models.dtos.RegisterDTO;
import com.example.backend.models.entities.UserEntity;

public interface IAuthService {

    String login (LoginDTO loginDto);
    UserEntity register(RegisterDTO registerDto);
    Boolean verifyToken(String token) throws Exception;
}
