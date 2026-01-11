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
    public ResponseEntity<LoginResponseDTO> refresh(HttpServletRequest httpServletRequest) {
        String refreshToken = Arrays.stream(httpServletRequest.getCookies()).filter(
                        cookie -> "refreshToken".equals(cookie.getName())).findFirst()
                .map(cookie -> cookie.getValue()).orElseThrow(() ->
                        new AuthenticationServiceException("Refresh Token Not found Inside Cookies"));

        LoginResponseDTO loginResponseDTO = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(loginResponseDTO);
    }
}
