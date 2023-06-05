package com.example.main_service.camunda.auth;

import com.example.main_service.model.ERole;
import com.example.main_service.model.Role;
import com.example.main_service.model.User;
import com.example.main_service.repository.RoleRepository;
import com.example.main_service.repository.UserRepository;
import com.example.main_service.security.CookUserDetails;
import com.example.main_service.security.CookUserDetailsService;
import com.example.main_service.security.JwtUtils;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SignUp implements JavaDelegate {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;
    private final CookUserDetailsService cookUserDetailsService;

    public SignUp(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, JwtUtils jwtUtils, CookUserDetailsService cookUserDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtUtils = jwtUtils;
        this.cookUserDetailsService = cookUserDetailsService;
    }

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String username = (String) delegateExecution.getVariable("username");
        String password = (String) delegateExecution.getVariable("password");
        String email = (String) delegateExecution.getVariable("email");
        System.out.println(username);
        if (userRepository.existsUserByLogin(username)) {
            throw new BpmnError("This login already exist");
        }
        if (userRepository.existsUserByEmail(email)) {
            throw new BpmnError("This email already exist");
        }

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository
                .findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new BpmnError("Роль USER не найдена!"));
        roles.add(userRole);
        User user = new User(username, password, email, roles);
        userRepository.save(user);
        List<GrantedAuthority> authorities = roles.stream()
                .map(roles2 -> new SimpleGrantedAuthority(roles2.getName().name()))
                .collect(Collectors.toList());

        String accessToken = jwtUtils.generateJWTToken(username, authorities, email);
        String refreshToken = jwtUtils.generateRefreshToken(username, authorities, email);

        delegateExecution.setVariable("accessToken", accessToken);
        delegateExecution.setVariable("refreshToken", refreshToken);
    }
}
