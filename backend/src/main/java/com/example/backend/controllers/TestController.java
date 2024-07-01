package com.example.backend.controllers;

import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/test")
@RequiredArgsConstructor
public class TestController {
    @GetMapping("/aaa")
    public String test(){
        return "Hello world";
    }

    @GetMapping("/reader")
    @PreAuthorize("hasAnyAuthority('READER')")
    public String testReader(){
        return "Hello reader";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String testAdmin(){
        return "Hello Admin";
    }

}
