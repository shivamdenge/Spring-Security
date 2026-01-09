package com.shivamdenge.Module4.service;

import com.shivamdenge.Module4.dto.SignupDTO;
import com.shivamdenge.Module4.dto.UserDTO;
import com.shivamdenge.Module4.entity.UserEntity;
import com.shivamdenge.Module4.exception.ResourceNotFoundException;
import com.shivamdenge.Module4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class userService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User Not found with email :" + username));
    }


    public UserDTO signUp(SignupDTO signupDTO) {
        Optional<UserEntity> user = userRepository.findByEmail(signupDTO.getEmail());
        if (user.isPresent()) {
            throw new BadCredentialsException("User is Already Present with email :" + signupDTO.getEmail());
        }


        UserEntity tobeCreatedUser = modelMapper.map(signupDTO, UserEntity.class);
        tobeCreatedUser.setPassword(passwordEncoder.encode(signupDTO.getPassword()));

        UserEntity saveUser = userRepository.save(tobeCreatedUser);

        return modelMapper.map(saveUser, UserDTO.class);
    }
}
