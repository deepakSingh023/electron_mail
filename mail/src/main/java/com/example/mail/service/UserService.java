package com.example.mail.service;

import com.example.mail.dto.LoginUser;
import com.example.mail.dto.RegisterResponse;
import com.example.mail.dto.RegisterUser;

public interface UserService {

    RegisterResponse registerUser(RegisterUser user);

    RegisterResponse loginUser(LoginUser user);

    String refreshAccessToken(String refreshToken);


}
