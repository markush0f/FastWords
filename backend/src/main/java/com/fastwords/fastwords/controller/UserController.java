package com.fastwords.fastwords.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastwords.fastwords.models.dtos.CreateUserDto;
import com.fastwords.fastwords.models.dtos.UserResponseDto;
import com.fastwords.fastwords.services.UserService;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }


    @PostMapping    ("")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody CreateUserDto createUserDto) {
        System.out.println("Received request to create user with details: " + createUserDto);
        UserResponseDto createdUser = userService.createUser(createUserDto);
        return ResponseEntity
            .created(java.net.URI.create("/" + createdUser.getId()))
            .body(createdUser);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
