package com.fastwords.fastwords.services;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.fastwords.fastwords.models.dtos.CreateUserDto;
import com.fastwords.fastwords.models.dtos.UserResponseDto;
import com.fastwords.fastwords.models.entities.User;
import com.fastwords.fastwords.repository.UserRepository;
import com.fastwords.fastwords.util.ResourceFinder;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ResourceFinder resourceFinder;

    public UserServiceImpl(UserRepository userRepository, ResourceFinder resourceFinder) {
        this.userRepository = userRepository;
        this.resourceFinder = resourceFinder;
    }

    @Override
    public UserResponseDto createUser(CreateUserDto createUserDto) {
        try {
            System.out.println("Creating user with details: " + createUserDto);
            User user = User.builder()
                    .username(createUserDto.getUsername())
                    .email(createUserDto.getEmail())
                    .password(createUserDto.getPassword())
                    .build();

            User savedUser = userRepository.save(user);

            return UserResponseDto.builder()
                    .id(savedUser.getId())
                    .username(savedUser.getUsername())
                    .email(savedUser.getEmail())
                    .build();

        } catch (DataAccessException e) {
            throw new RuntimeException("Database error occurred while creating user: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user: " + e.getMessage(), e);
        }
    }

    @Override
    public UserResponseDto updateUser(Long userId, String username, String password) {
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public void deleteUser(Long userId) {
        try {
            userRepository.deleteById(userId);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage());
        }
    }

    @Override
    public UserResponseDto getUserById(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
            return UserResponseDto.builder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Error retrieving user: " + e.getMessage());
        }
    }

    @Override
    public User findUserOrThrowNotFound(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

}
