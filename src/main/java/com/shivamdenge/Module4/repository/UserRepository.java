package com.shivamdenge.Module4.repository;

import com.shivamdenge.Module4.entity.UserEntity;
import org.modelmapper.internal.bytebuddy.dynamic.DynamicType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);
    
}