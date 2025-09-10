package com.matalban.pollsystem.services;

import com.matalban.pollsystem.dto.LoginRequest;
import com.matalban.pollsystem.dto.LoginResponse;
import com.matalban.pollsystem.dto.RegistrationRequest;
import org.springframework.web.bind.annotation.RequestBody;

public interface  AuthService {

    LoginResponse login(@RequestBody LoginRequest loginRequest);
    String register(@RequestBody RegistrationRequest registrationRequest);
}
