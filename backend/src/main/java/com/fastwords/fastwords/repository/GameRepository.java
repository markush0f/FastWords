package com.fastwords.fastwords.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastwords.fastwords.common.enums.GameStatus;
import com.fastwords.fastwords.models.entities.Game;
import com.fastwords.fastwords.models.entities.User;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByPlayer1OrPlayer2(User player1, User player2);
    List<Game> findByGameStatus(GameStatus gameStatus);
}
