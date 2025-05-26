package com.fastwords.fastwords.services;

import com.fastwords.fastwords.models.dtos.CreateGameDto;
import com.fastwords.fastwords.models.dtos.GameResponseDto;

public interface GameService
 {
    GameResponseDto createGame(CreateGameDto createGameDto);

    void deleteGame(Long roomId, Long userId);
}
