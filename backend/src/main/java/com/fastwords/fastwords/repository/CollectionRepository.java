package com.fastwords.fastwords.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastwords.fastwords.models.entities.Collection;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
    Optional<Collection> findByName(String name);
}
