package com.example.mail;

import com.example.mail.dto.RegisterResponse;
import com.example.mail.dto.RegisterUser;
import com.example.mail.entity.User;
import com.example.mail.exception_handling.exception.UserAlreadyExist;
import com.example.mail.repository.UserRepository;
import com.example.mail.service.UserServiceImpl;
import com.example.mail.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void registerUserShouldFailWhenUserAlreadyExists() {

        RegisterUser data = new RegisterUser(
                "alice",
                "alice@example.com",
                "StrongPassword123!",
                "encrypted-private-key",
                "public-key"
        );

        when(userRepository.existsByUsernameOrEmail(
                "alice",
                "alice@example.com"
        )).thenReturn(true);

        assertThrows(
                UserAlreadyExist.class,
                () -> userService.registerUser(data)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUserSave() {

        RegisterUser data = new RegisterUser(
                "alice",
                "alice@example.com",
                "StrongPassword123!",
                "encrypted-private-key",
                "public-key"
        );

        when(userRepository.existsByUsernameOrEmail(
                "alice",
                "alice@example.com"
        )).thenReturn(false);

        when(passwordEncoder.encode(data.password()))
                .thenReturn("encoded-password");

        when(jwtUtil.createAccessToken(any(), any()))
                .thenReturn("access-token");

        when(jwtUtil.createRefreshToken(any()))
                .thenReturn("refresh-token");

        RegisterResponse response = userService.registerUser(data);

        assertNotNull(response);
        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());

        verify(userRepository).save(any(User.class));
    }
}
