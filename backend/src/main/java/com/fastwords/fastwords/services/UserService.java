package com.fastwords.fastwords.services;

import org.springframework.http.ResponseEntity;

import com.fastwords.fastwords.models.entities.User;

public class UserService implements UserServiceImpl {

    @Override
    public ResponseEntity<User> createUser(String username, String password) {
        throw new UnsupportedOperationException("Unimplemented method 'createUser'");
    }

    @Override
    public ResponseEntity<User> updateUser(Long userId, String username, String password) {
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public ResponseEntity<?> deleteUser(Long userId) {
        throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
    }

    @Override
    public ResponseEntity<User> getUserById(Long userId) {
        throw new UnsupportedOperationException("Unimplemented method 'getUserById'");
    }


    
}
