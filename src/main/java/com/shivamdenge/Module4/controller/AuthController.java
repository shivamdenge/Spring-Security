package com.shivamdenge.Module4.controller;

import com.shivamdenge.Module4.dto.LoginDTO;
import com.shivamdenge.Module4.dto.LoginResponseDTO;
import com.shivamdenge.Module4.dto.SignupDTO;
import com.shivamdenge.Module4.dto.UserDTO;
import com.shivamdenge.Module4.service.AuthService;
import com.shivamdenge.Module4.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody SignupDTO signupDTO) {
        return ResponseEntity.ok(userService.signUp(signupDTO));
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        LoginResponseDTO loginResponseDTO = authService.login(loginDTO);

        Cookie cookie = new Cookie("refreshToken", loginResponseDTO.getRefreshToken());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return ResponseEntity.ok(loginResponseDTO);
    }


    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(HttpServletRequest request) {

        // STEP 1:
        // Get ALL cookies sent by the browser.
        // Browser automatically sends cookies (frontend does nothing)
        String refreshToken = Arrays.stream(request.getCookies())

                // STEP 2:
                // Find the cookie named "refreshToken"
                .filter(cookie -> "refreshToken".equals(cookie.getName()))

                // STEP 3:
                // Extract its value (the JWT string)
                .findFirst().map(cookie -> cookie.getValue())

                // STEP 4:
                // If cookie is missing -> user is logged out
                .orElseThrow(() -> new AuthenticationServiceException("Refresh Token Not found Inside Cookies"));

        // STEP 5:
        // Pass refresh token to service
        // Controller does NOT validate token
        LoginResponseDTO response = authService.refreshToken(refreshToken);

        // STEP 6:
        // Send new access token back to frontend
        return ResponseEntity.ok(response);
    }

}

/*
1. User logs in
   → gets accessToken + refreshToken

2. User uses app
   → accessToken sent in headers

3. accessToken expires
   → backend returns 401

4. Frontend calls /refresh
   → browser auto-sends refreshToken cookie

5. Backend:
   → validates refresh token
   → generates new access token

6. User continues
*/
