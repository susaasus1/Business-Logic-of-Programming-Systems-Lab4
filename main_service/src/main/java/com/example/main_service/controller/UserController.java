package com.example.main_service.controller;

import com.example.main_service.mapper.UserDTOMapper;
import com.example.main_service.dto.request.RefreshTokenRequest;
import com.example.main_service.dto.request.SignInRequest;
import com.example.main_service.dto.request.SignUpRequest;
import com.example.main_service.dto.response.AccessAndRefreshToken;
import com.example.main_service.dto.response.NewTokenResponse;
import com.example.main_service.dto.response.UserResponse;
import com.example.main_service.model.ERole;
import com.example.main_service.model.User;
import com.example.main_service.service.RefreshTokenService;
import com.example.main_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final UserDTOMapper userDTOMapper;

    public UserController(UserService userService,
                          RefreshTokenService refreshTokenService,
                          UserDTOMapper userDTOMapper) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;
        this.userDTOMapper = userDTOMapper;

    }

    @PostMapping("login")
    public AccessAndRefreshToken authUser(@Valid @RequestBody SignInRequest signInRequest) {
        return userService.authUser(signInRequest);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        User user = userService.saveNewUser(signUpRequest, ERole.ROLE_USER);
        return userDTOMapper.apply(user);
    }


    @PostMapping("refreshToken")
    @ResponseStatus(HttpStatus.CREATED)
    public NewTokenResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenService.createNewToken(refreshTokenRequest);
    }

    @PostMapping("admin-create")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse registerAdmin(@Valid @RequestBody SignUpRequest signUpRequest) {
        User admin = userService.saveNewUser(signUpRequest, ERole.ROLE_ADMIN);
        return userDTOMapper.apply(admin);
    }

    @PostMapping("logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, null);
    }
}
