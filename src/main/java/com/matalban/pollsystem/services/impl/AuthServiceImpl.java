package com.matalban.pollsystem.services.impl;


import com.matalban.pollsystem.domain.UserAccount;
import com.matalban.pollsystem.api.v0.dto.LoginRequest;
import com.matalban.pollsystem.api.v0.dto.LoginResponse;
import com.matalban.pollsystem.api.v0.dto.RegistrationRequest;
import com.matalban.pollsystem.jwt.JwtUtils;
import com.matalban.pollsystem.repositories.UserRepository;
import com.matalban.pollsystem.services.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository,  PasswordEncoder passwordEncoder, JwtUtils jwtUtils, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;

    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

       Authentication authentication;

       try {
           authentication = authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                           loginRequest.getPassword()));
       }catch(AuthenticationException e){
           Map<String, Object> map = new HashMap<>();
           map.put("message", e.getMessage());
           map.put("status", false);
           return new LoginResponse(null, null);

       }

       SecurityContextHolder.getContext().setAuthentication(authentication);
       UserDetails userDetails = (UserDetails) authentication.getPrincipal();
       System.out.println(userDetails.getUsername());

       String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

       return new LoginResponse(userDetails.getUsername(), jwtToken);
    }

    @Override
    public String register(RegistrationRequest registrationRequest) {
        System.out.println("service Reg started");
        if(userRepository.existsByUsername(registrationRequest.getUsername()))
            return "Impossibile creare l'utente";


        UserAccount user = new UserAccount();
        user.setUsername(registrationRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setEmail(registrationRequest.getEmail());
        userRepository.save(user);

        System.out.println("service Reg executed");
        return "utente creato con successo";
    }
}