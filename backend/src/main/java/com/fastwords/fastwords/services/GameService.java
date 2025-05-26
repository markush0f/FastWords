package com.fastwords.fastwords.services;

import com.fastwords.fastwords.models.dtos.CreateGameDto;

public interface GameService
 {
    CreateGameDto createGame(CreateGameDto createGameDto);

    void deleteGame(Long roomId, Long userId);
}
