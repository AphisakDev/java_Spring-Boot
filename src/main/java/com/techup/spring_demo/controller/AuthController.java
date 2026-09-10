package com.techup.spring_demo.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techup.spring_demo.security.JwtService;
import com.techup.spring_demo.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    // ✅ Register
    @PostMapping("/register")
    public String register(@RequestBody Map<String, String> req) {
        return authService.register(req.get("email"), req.get("password"));
    }

    // ✅ Login
    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> req) {
        String token = authService.login(req.get("email"), req.get("password"));
        return Map.of("token", token);
    }

    // ✅ /auth/me — แสดง email จาก token
    @GetMapping("/me")
    public Map<String, String> me(@RequestHeader("Authorization") String header) {
        String token = header.replace("Bearer ", "");
        String email = jwtService.extractEmail(token);
        return Map.of("email", email);
    }
}
