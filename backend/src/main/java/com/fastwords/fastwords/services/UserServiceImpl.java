package com.fastwords.fastwords.services;

import org.springframework.http.ResponseEntity;

import com.fastwords.fastwords.models.entities.User;

public interface UserServiceImpl {

    ResponseEntity<User> createUser(String username, String password);
    ResponseEntity<User> updateUser(Long userId, String username, String password);
    ResponseEntity<?> deleteUser(Long userId);
    ResponseEntity<User> getUserById(Long userId);
    
}
