package com.example.backend.controllers.user;

import com.example.backend.models.responses.HttpResponse;
import com.example.backend.models.responses.UserResponse;
import com.example.backend.services.user.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/user")
@RequiredArgsConstructor
public class UserController {
    private final IAuthService authService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('READER', 'ADMIN')")
    public ResponseEntity<HttpResponse> getById(
            @PathVariable("id") Long id
    ) {
        try {
            UserResponse exitingUser = authService.getById(id);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .message("Get user by id success")
                            .data(Map.of("user", exitingUser))
                            .build());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message(ex.getMessage())
                            .build());
        }
    }
}
