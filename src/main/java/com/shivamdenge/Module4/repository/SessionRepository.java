package com.shivamdenge.Module4.repository;

import com.shivamdenge.Module4.entity.SessionEntity;
import com.shivamdenge.Module4.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {

    List<SessionEntity> findByUser(UserEntity user);

    Optional<SessionEntity> findByRefreshToken(String refreshToken);
}
