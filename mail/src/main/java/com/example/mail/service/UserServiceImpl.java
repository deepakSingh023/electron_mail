package com.example.mail.service;


import com.example.mail.dto.LoginUser;
import com.example.mail.dto.RegisterResponse;
import com.example.mail.dto.RegisterUser;
import com.example.mail.entity.User;
import com.example.mail.exception_handling.exception.UserAlreadyExist;
import com.example.mail.exception_handling.exception.UserDoesntExist;
import com.example.mail.exception_handling.exception.WrongPassword;
import com.example.mail.repository.UserRepository;
import com.example.mail.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;


    @Override
    public  RegisterResponse registerUser(RegisterUser data){

        if(userRepository.existsByUsernameOrEmail(data.username(), data.email())){
            throw new UserAlreadyExist("The user already exists");
        }

        User user = User.builder()
                .username(data.username())
                .email(data.email())
                .password(passwordEncoder.encode(data.password()))
                .public_key(data.public_key())
                .private_key(data.private_key())
                .created_at(Instant.now())
                .build();

        userRepository.save(user);

        String accessToken = jwtUtil.createAccessToken(user.getId(),data.email());

        String refreshToken = jwtUtil.createRefreshToken(user.getId());

        return new RegisterResponse(user.getId() ,accessToken,refreshToken);

    }

    @Override
    public RegisterResponse loginUser(LoginUser data){

        User user = userRepository.findByEmail(data.email())
                .orElseThrow(() -> new UserDoesntExist("user doesn't exist"));

        if(!passwordEncoder.matches(data.password(), user.getPassword())){
            throw new WrongPassword("Password doesn't match");
        }

        String accessToken = jwtUtil.createAccessToken(user.getId(),data.email());

        String refreshToken = jwtUtil.createRefreshToken(user.getId());

        return  new RegisterResponse(user.getId(), accessToken , refreshToken);

    }

    @Override
    public String refreshAccessToken(String refreshToken){
        try {
            Claims claims = jwtUtil.extractClaims(refreshToken);

            String tokenType = claims.get("token_type", String.class);

            if (!"refresh".equals(tokenType)) {
                throw new IllegalArgumentException("Invalid refresh token");
            }

            String userId = claims.getSubject();

            User user = userRepository.findById(userId)
                    .orElseThrow(() ->
                            new UserDoesntExist("User doesn't exist"));

            return jwtUtil.createAccessToken(user.getId(), user.getEmail());

        } catch (JwtException e) {
            throw new IllegalArgumentException(
                    "Invalid or expired refresh token"
            );
        }


    }


}
