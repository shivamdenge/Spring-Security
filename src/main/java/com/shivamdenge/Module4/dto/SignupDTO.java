package com.shivamdenge.Module4.dto;

import com.shivamdenge.Module4.entity.enums.Roles;
import lombok.Data;

import java.util.Set;

@Data
public class SignupDTO {
    private String name;
    private String email;
    private String password;
    private Set<Roles> role;
}
