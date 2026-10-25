package com.example.mail.controller;


import com.example.mail.dto.LoginUser;
import com.example.mail.dto.RefreshResponse;
import com.example.mail.dto.RegisterResponse;
import com.example.mail.dto.RegisterUser;
import com.example.mail.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid  @RequestBody RegisterUser user
            ){

        RegisterResponse res = userService.registerUser(user);

        return ResponseEntity.ok(res);

    }

    @PostMapping("/login")
    public ResponseEntity<RegisterResponse> login(
            @Valid  @RequestBody LoginUser user
    ){

        RegisterResponse res = userService.loginUser(user);

        return ResponseEntity.ok(res);

    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(
            @CookieValue(name = "refreshToken", required = false)
            String refreshToken) {

        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String accessToken = userService.refreshAccessToken(refreshToken);

        return ResponseEntity.ok(new RefreshResponse(accessToken));
    }



}
