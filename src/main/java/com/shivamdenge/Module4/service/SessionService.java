package com.shivamdenge.Module4.service;

import com.shivamdenge.Module4.entity.SessionEntity;
import com.shivamdenge.Module4.entity.UserEntity;
import com.shivamdenge.Module4.repository.SessionRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepository sessionRepository;

    public final int SESSION_LIMIT = 2;

    public void generateSession(UserEntity user, String refreshToken) {
        List<SessionEntity> userSession = sessionRepository.findByUser(user);
        if (userSession.size() == SESSION_LIMIT) {
            userSession.sort(Comparator.comparing(SessionEntity::getLastUsedAt));
            SessionEntity leastRecentlyUsedSession = userSession.getFirst();
            sessionRepository.delete(leastRecentlyUsedSession);
        }

        SessionEntity session = SessionEntity.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();
        sessionRepository.save(session);
    }

    public void validateSession(String refreshToken) {
        SessionEntity session = sessionRepository.findByRefreshToken(refreshToken).orElseThrow(() ->
                new SessionAuthenticationException("Session is Not Valid"));
        session.setLastUsedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }
}
