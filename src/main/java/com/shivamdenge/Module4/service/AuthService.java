package com.shivamdenge.Module4.service;

import com.shivamdenge.Module4.dto.LoginDTO;
import com.shivamdenge.Module4.dto.LoginResponseDTO;
import com.shivamdenge.Module4.entity.UserEntity;
import com.shivamdenge.Module4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final SessionService sessionService;


    public LoginResponseDTO login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
        );

        UserEntity user = (UserEntity) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        sessionService.generateSession(user,refreshToken);


        return new LoginResponseDTO(user.getId(), accessToken, refreshToken);
    }

    /**
     * This method is called AFTER the user is already logged in once.
     * It is NOT login. It is token renewal.
     */
    public LoginResponseDTO refreshToken(String refreshToken) {

        // STEP 1:
        // Decode the refresh token.
        // This automatically:
        // - verifies the JWT signature
        // - checks if token is expired
        // - extracts the subject (userId)
        Long userId = jwtService.getUserIdFromToken(refreshToken);


        sessionService.validateSession(refreshToken);

        // STEP 2:
        // Fetch the user from database using userId
        // This confirms the user still exists / is valid
        UserEntity user = userService.getUserById(userId);

        // STEP 3:
        // Generate a BRAND-NEW access token
        // Old access token was expired
        String accessToken = jwtService.generateAccessToken(user);

        // STEP 4:
        // Return new access token
        // Refresh token stays SAME
        return new LoginResponseDTO(userId, accessToken, refreshToken);
    }

}
