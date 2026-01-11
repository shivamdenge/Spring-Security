package com.shivamdenge.Module4.controller;

import com.shivamdenge.Module4.dto.LoginDTO;
import com.shivamdenge.Module4.dto.SignupDTO;
import com.shivamdenge.Module4.dto.UserDTO;
import com.shivamdenge.Module4.service.AuthService;
import com.shivamdenge.Module4.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody SignupDTO signupDTO) {
        return userService.signup(signupDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO, HttpServletResponse httpServletResponse) {
        String token = authService.login(loginDTO);
        Cookie cookie = new Cookie("Token", token);
        cookie.setHttpOnly(true);
        httpServletResponse.addCookie(cookie);

        return ResponseEntity.ok(token);
    }


}
