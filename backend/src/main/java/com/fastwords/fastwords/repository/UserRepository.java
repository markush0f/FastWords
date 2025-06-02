package com.fastwords.fastwords.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastwords.fastwords.models.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}