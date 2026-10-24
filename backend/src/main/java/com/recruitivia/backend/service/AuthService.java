package com.recruitivia.backend.service;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.recruitivia.backend.dto.AuthResponse;
import com.recruitivia.backend.dto.LoginRequest;
import com.recruitivia.backend.dto.RegisterRequest;
import com.recruitivia.backend.entity.Role;
import com.recruitivia.backend.entity.Session;
import com.recruitivia.backend.entity.User;
import com.recruitivia.backend.repository.RoleRepository;
import com.recruitivia.backend.repository.SessionRepository;
import com.recruitivia.backend.repository.UserRepository;
import com.recruitivia.backend.security.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       SessionRepository sessionRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public void register(RegisterRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
        }
        if (request.getRole() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role is required");
        }

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found"));

        User user = new User();
        user.setName(request.getName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        userRepository.save(user);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        String email = request.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(this::invalidLogin);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw invalidLogin();
        }

        String roleName = user.getRole().getName();
        String token = jwtService.generateToken(user.getEmail(), roleName);

        Session session = new Session();
        session.setUser(user);
        session.setToken(token);
        session.setExpiresAt(LocalDateTime.ofInstant(
                jwtService.extractExpiration(token).toInstant(), ZoneId.systemDefault()));
        sessionRepository.save(session);

        return new AuthResponse(token, user.getName(), user.getEmail(), roleName);
    }

    // Same message for wrong email and wrong password, so nobody can discover which emails exist
    private ResponseStatusException invalidLogin() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }
}