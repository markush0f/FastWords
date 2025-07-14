package com.fastwords.fastwords.services;

import com.fastwords.fastwords.models.dtos.CreateGameDto;
import com.fastwords.fastwords.models.dtos.GameResponseDto;
import com.fastwords.fastwords.models.entities.Game;

public interface GameService
 {
    GameResponseDto createGame(CreateGameDto createGameDto);

    void deleteGame(Long roomId, Long userId);

    GameResponseDto getGameById(Long id);

    Game findGameOrThrowNotFound(Long id);  

}
