package com.fastwords.fastwords.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastwords.fastwords.models.dtos.CreateGameDto;
import com.fastwords.fastwords.models.dtos.GameResponseDto;
import com.fastwords.fastwords.services.CollectionServiceImpl;
import com.fastwords.fastwords.services.GameService;

@RestController()
@RequestMapping("/api/v1/game")
public class GameController {

    private final GameService gameService;
    private static final Logger logger = LoggerFactory.getLogger(CollectionServiceImpl.class);

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("")
    public ResponseEntity<GameResponseDto> postMethodName(@RequestBody CreateGameDto createGameDto) {
        GameResponseDto createdGame = gameService.createGame(createGameDto);
        return ResponseEntity
                .created(URI.create("/" + createdGame.getId()))
                .body(createdGame);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameResponseDto> getGameById(@PathVariable Long id) {
        GameResponseDto game = gameService.getGameById(id);
        return ResponseEntity.ok(game);
    }

}
