package com.fastwords.fastwords.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController(value = "/api/v1/user")
public class UserController {

    @GetMapping("")
    public String  getUsers() {
        return "Hello World";
    }
}
