package com.example.mail.service;


import com.example.mail.dto.RegisterResponse;
import com.example.mail.dto.RegisterUser;
import com.example.mail.entity.User;
import com.example.mail.exception_handling.exception.UserAlreadyExist;
import com.example.mail.repository.UserRepository;
import com.example.mail.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;


    @Override
    public  RegisterResponse register_user(RegisterUser data){

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

        String token = jwtUtil.

        return new RegisterResponse(user.getId() ,token , user.getPrivate_key());

    }
}
