package org.example.BlogEngine.controller;


import org.example.BlogEngine.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// обрабатывает все запросы /api/auth/*
@RestController
public class ApiAuthController {


    private final AuthService authService;

    public ApiAuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/api/auth/check")
    private ResponseEntity<?> getAuthCheck () {
        System.out.println("Method getAuthCheck is activated.");
        return authService.getAuthCheck();
    }
}
