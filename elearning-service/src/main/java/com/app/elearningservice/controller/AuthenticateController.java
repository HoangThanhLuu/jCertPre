package com.app.elearningservice.controller;

import com.app.elearningservice.model.ResponseContainer;
import com.app.elearningservice.payload.LoginPayload;
import com.app.elearningservice.payload.RegisterPayload;
import com.app.elearningservice.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
@RequestMapping("/auth")
public class AuthenticateController {
    private final UserService authService;

    public AuthenticateController(UserService authService) {
        this.authService = authService;
    }

    @PostMapping("login")
    public Object login(
            @Valid @RequestBody LoginPayload payload
    ) {
        return ResponseContainer.success(authService.authenticate(payload.email(), payload.password()));
    }

    @PostMapping("register")
    public Object register(@Valid @RequestBody RegisterPayload payload) {
        try {
            if (authService.validateEmailExist(payload.email())) {
                return ResponseContainer.failure("Email already exists");
            }
            authService.register(payload);
            return ResponseContainer.success("Register successfully");
        } catch (Exception e) {
            return ResponseContainer.failure(e.getMessage());
        }
    }

}
