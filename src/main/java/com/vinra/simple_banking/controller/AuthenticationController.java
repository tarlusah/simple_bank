package com.vinra.simple_banking.controller;

import com.vinra.simple_banking.controller.converter.AuthenticationDtoConverter;
import com.vinra.simple_banking.controller.dto.LoginRequest;
import com.vinra.simple_banking.controller.dto.LoginResponse;
import com.vinra.simple_banking.controller.dto.RegisterRequest;
import com.vinra.simple_banking.controller.dto.RegisterResponse;
import com.vinra.simple_banking.service.UserService;
import com.vinra.simple_banking.model.User;
import com.vinra.simple_banking.utils.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final AuthenticationDtoConverter converter;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @PostMapping(path = "/signup")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest registerRequest
    ){
        try{
            registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            User user = userService.registerUser(converter.convertUser(registerRequest));

            RegisterResponse response = new RegisterResponse();
            response.setUser(user);
            response.setMessage("Successfully registered");
            return ResponseEntity.ok(response);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest loginRequest
    ){
        try {
            Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );
            var token = jwtUtils.generateJwtToken(authenticate);
            LoginResponse response = new LoginResponse("Login Success", token);
            return ResponseEntity.ok(response);
        }catch (Exception e) {
//            e.printStackTrace();
            throw e;
        }
    }


}
