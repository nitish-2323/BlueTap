
package com.javaRestApi.BankingApplicationRestApi.Controller.Auth;

import com.javaRestApi.BankingApplicationRestApi.Model.Auth.LoginRequest;
import com.javaRestApi.BankingApplicationRestApi.security.JwtUtil;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        String token = jwtUtil.generateToken(authentication.getName());

        return ResponseEntity.ok(Map.of(
                "error", false,
                "message", "Login successful",
                "tokenType", "Bearer",
                "accessToken", token,
                "expiresIn", 900
        ));
    }
}
