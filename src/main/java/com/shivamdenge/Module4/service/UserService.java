package com.shivamdenge.Module4.service;

import com.shivamdenge.Module4.dto.LoginDTO;
import com.shivamdenge.Module4.dto.SignupDTO;
import com.shivamdenge.Module4.dto.UserDTO;
import com.shivamdenge.Module4.entity.UserEntity;
import com.shivamdenge.Module4.exception.ResourceNotFoundException;
import com.shivamdenge.Module4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.type.descriptor.java.ImmutableObjectArrayMutabilityPlan;
import org.modelmapper.ModelMapper;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User Not found"));
    }

    public ResponseEntity<UserDTO> signup(SignupDTO signupDTO) {
        Optional<UserEntity> user = userRepository.findByEmail(signupDTO.getEmail());
        if (user.isPresent()) {
            throw new ResourceNotFoundException("User is Already Exist");
        }

        UserEntity tobeCreateUser = modelMapper.map(signupDTO, UserEntity.class);
        tobeCreateUser.setPassword(passwordEncoder.encode(tobeCreateUser.getPassword()));

        UserEntity savedUser = userRepository.save(tobeCreateUser);
        return ResponseEntity.ok(modelMapper.map(savedUser, UserDTO.class));
    }
}


