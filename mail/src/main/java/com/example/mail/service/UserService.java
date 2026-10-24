package com.example.mail.service;

import com.example.mail.dto.RegisterResponse;
import com.example.mail.dto.RegisterUser;

public interface UserService {

    RegisterResponse register_user(RegisterUser user);
}
