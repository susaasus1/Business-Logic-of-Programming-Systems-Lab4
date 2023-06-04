package com.example.main_service.service;

import com.example.main_service.dto.request.SignInRequest;
import com.example.main_service.dto.request.SignUpRequest;
import com.example.main_service.dto.response.AccessAndRefreshToken;
import com.example.main_service.model.ERole;
import com.example.main_service.model.Role;
import com.example.main_service.model.User;
import com.example.main_service.repository.RoleRepository;
import com.example.main_service.repository.UserRepository;
import com.example.main_service.exception.ResourceAlreadyExistException;
import com.example.main_service.exception.ResourceNotFoundException;
import com.example.main_service.security.CookUserDetails;
import com.example.main_service.security.CookUserDetailsService;
import com.example.main_service.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    private final CookUserDetailsService cookUserDetailsService;



    public UserService(AuthenticationManager authenticationManager,
                       JwtUtils jwtUtils, UserRepository userRepository,
                       PasswordEncoder passwordEncoder, RoleRepository roleRepository, CookUserDetailsService cookUserDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.cookUserDetailsService = cookUserDetailsService;
    }

    public AccessAndRefreshToken authUser(SignInRequest signInRequest) {
        CookUserDetails userDetails = (CookUserDetails) cookUserDetailsService.loadUserByUsername(signInRequest.getLogin());
        String accessToken = jwtUtils.generateJWTToken(userDetails.getUsername(), userDetails.getAuthorities(), userDetails.getEmail());
        String refreshToken = jwtUtils.generateRefreshToken(userDetails.getUsername(),  userDetails.getAuthorities(), userDetails.getEmail());
        return new AccessAndRefreshToken(accessToken, refreshToken);
    }

    public User findUserByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException("Пользователь с логином " + login + " не существует"));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Пользователь с почтой " + email + " не существует"));
    }


    public boolean checkLoginOnExist(String login) {
        return userRepository.existsUserByLogin(login);
    }

    public boolean checkEmailOnExist(String email) {
        return userRepository.existsUserByEmail(email);
    }


    public User saveNewUser(SignUpRequest signUpRequest, ERole role) {
        if (checkLoginOnExist(signUpRequest.getLogin()))
            throw new ResourceAlreadyExistException("Этот логин уже занят! Попробуйте другой");

        if (checkEmailOnExist(signUpRequest.getEmail()))
            throw new ResourceAlreadyExistException("Эта почта уже занята! Попробуйте другую");

        Set<Role> roles = new HashSet<>();
        switch (role) {
            case ROLE_USER -> {
                Role userRole = roleRepository
                        .findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new ResourceNotFoundException("Роль USER не найдена!"));
                roles.add(userRole);
            }
            case ROLE_ADMIN -> {
                Role adminRole = roleRepository
                        .findByName(ERole.ROLE_ADMIN)
                        .orElseThrow(() -> new ResourceNotFoundException("Роль ADMIN не найдена!"));
                roles.add(adminRole);
            }
            default -> throw new ResourceNotFoundException("Роль не найдена!");
        }

        User user = new User(signUpRequest.getLogin(), passwordEncoder.encode(signUpRequest.getPassword()),
                signUpRequest.getEmail(), roles);

        userRepository.save(user);
        return user;
    }

    public void incrementCulinaryNewsCounter(String user_login) {
        User user = findUserByLogin(user_login);
        user.setCulinaryNewsCount(user.getCulinaryNewsCount() + 1);
        userRepository.save(user);
    }
}
