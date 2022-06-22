package org.example.BlogEngine.service;


import org.example.BlogEngine.model.Session;
import org.example.BlogEngine.model.User;
import org.example.BlogEngine.repository.SessionRepository;
import org.example.BlogEngine.repository.UserRepository;
import org.example.BlogEngine.response.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final HttpSession httpSession;

    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, HttpSession httpSession) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.httpSession = httpSession;
    }

    public ResponseEntity<?> getAuthCheck() {
        AuthResponse authResponse = new AuthResponse();
        Optional<Integer> userIdOptinal;
        User u;
        String sessionName = httpSession.getId();
        boolean isSession = sessionRepository.findAll().stream()
                .anyMatch(s -> s.getSessionName().equals(sessionName));
        if (isSession) {
            userIdOptinal = sessionRepository.findAll().stream()
                    .filter(s -> s.getSessionName().equals(sessionName))
                    .map(Session::getUserId).findAny();
            if (userIdOptinal.isPresent()) {
                int userId = userIdOptinal.get();
                u = userRepository.getOne(userId);
                LinkedHashMap<String, Object> user = new LinkedHashMap<>();
                user.put("id", userIdOptinal.get());
                user.put("name", u.getName());
                user.put("photo", u.getPhoto());
                user.put("email", u.getEmail());
                user.put("moderation", u.getIsModerator());
                user.put("moderationCount", getModerationCount(userId));
                user.put("settings", u.getIsModerator());
                authResponse.setResult(true);
                authResponse.setUser(user);
                return new ResponseEntity<>(authResponse, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User is not authorized.", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("Session is not open.", HttpStatus.OK);
        }
    }

    private int getModerationCount(Integer userId) {
        return userRepository.getModerationCount(userId);
    }
}
