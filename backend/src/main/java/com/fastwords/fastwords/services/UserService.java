package com.fastwords.fastwords.services;

import com.fastwords.fastwords.models.dtos.CreateUserDto;
import com.fastwords.fastwords.models.dtos.UserResponseDto;
import com.fastwords.fastwords.models.entities.User;

public interface UserService {

    UserResponseDto createUser(CreateUserDto createUserDto);

    UserResponseDto updateUser(Long userId, String username, String password);

    void deleteUser(Long userId);

    UserResponseDto getUserById(Long userId);

    User findUserOrThrowNotFound(Long userId);

}
