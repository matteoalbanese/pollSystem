package com.matalban.pollsystem.controllers;



import com.matalban.pollsystem.api.v0.dto.LoginRequest;
import com.matalban.pollsystem.api.v0.dto.LoginResponse;
import com.matalban.pollsystem.api.v0.dto.RegistrationRequest;
import com.matalban.pollsystem.api.v0.dto.ValidationErrorResponse;

import com.matalban.pollsystem.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/rest/api/v0")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest registrationRequest, HttpServletRequest request) {
        System.out.println("controller Reg started");
        String message = authService.register(registrationRequest);
        if(message.equals("Impossibile creare l'utente")){
            LocalDateTime now = LocalDateTime.now();
            return new ResponseEntity<>(new ValidationErrorResponse(now.toString(),message,request.getRequestURI()), HttpStatus.BAD_REQUEST);
        }
        System.out.println("controller Reg successfully executed");
        return new ResponseEntity<>("utente creato con successo", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest ){
        LoginResponse response = authService.login(loginRequest);

        return new ResponseEntity<>(response , HttpStatus.OK);
    }




}
