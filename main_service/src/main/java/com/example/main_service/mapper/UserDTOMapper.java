package com.example.main_service.mapper;


import com.example.main_service.dto.response.UserResponse;
import com.example.main_service.model.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;


@Component
public class UserDTOMapper implements Function<User, UserResponse> {

    @Override
    public UserResponse apply(User user) {
        return new UserResponse(
                user.getLogin(),
                user.getEmail(),
                user.getRoles()
        );
    }
}
