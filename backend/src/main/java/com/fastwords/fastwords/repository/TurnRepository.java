package com.fastwords.fastwords.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastwords.fastwords.models.entities.Game;
import com.fastwords.fastwords.models.entities.Turn;
import com.fastwords.fastwords.models.entities.User;

public interface TurnRepository extends JpaRepository<Turn, Long> {
    List<Turn> findByGameOrderByCreatedAtAsc(Game game);
    List<Turn> findByGameAndUser(Game game, User user);
    boolean existsByGameAndWordIgnoreCase(Game game, String word);
}