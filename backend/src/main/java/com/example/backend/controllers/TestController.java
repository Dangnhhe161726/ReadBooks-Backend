package com.example.backend.controllers;

import com.example.backend.models.dtos.NotificattionDTO;
import com.example.backend.models.responses.HttpResponse;
import com.example.backend.services.firebase.IFirebaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("${api.prefix}/test")
@RequiredArgsConstructor
public class TestController {
    private final IFirebaseService firebaseService;

    @GetMapping("/aaa")
    public String test() {
        return "Hello world";
    }

    @GetMapping("/reader")
    @PreAuthorize("hasAnyAuthority('READER')")
    public String testReader() {
        return "Hello reader";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String testAdmin() {
        return "Hello Admin";
    }

    @PostMapping("/firebase")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<HttpResponse> createNotification(@RequestBody NotificattionDTO notificate) {
        try {
            notificate.setCreateTime(LocalDateTime.now().toString());
            firebaseService.createNotification(notificate);
            return ResponseEntity.ok().body(
                    HttpResponse.builder()
                            .status(HttpStatus.OK)
                            .message("Create notificate success")
                            .build()
            );
        } catch (Exception e) {
            // Handle exceptions
            return ResponseEntity.badRequest().body(
                    HttpResponse.builder()
                            .message(e.getMessage())
                            .build()
            );
        }
    }
}
