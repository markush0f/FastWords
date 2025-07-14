package com.fastwords.fastwords.websocket;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.fastwords.fastwords.common.enums.GameStatus;
import com.fastwords.fastwords.models.entities.Game;
import com.fastwords.fastwords.models.entities.UsedWord;
import com.fastwords.fastwords.models.entities.Word;
import com.fastwords.fastwords.repository.GameRepository;
import com.fastwords.fastwords.repository.UsedWordRepository;
import com.fastwords.fastwords.repository.WordRepository;
import com.fastwords.fastwords.services.CollectionServiceImpl;
import com.fastwords.fastwords.services.GameService;

@Service
public class GameSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final GameService gameService;
    private final WordRepository wordRepository;
    private final UsedWordRepository usedWordRepository;
    private final GameRepository gameRepository;
    private static final Logger logger = LoggerFactory.getLogger(CollectionServiceImpl.class);

    public GameSocketService(
            SimpMessagingTemplate messagingTemplate,
            GameService gameService,
            WordRepository wordRepository,
            UsedWordRepository usedWordRepository,
            GameRepository gameRepository
    ) {
        this.messagingTemplate = messagingTemplate;
        this.gameService = gameService;
        this.wordRepository = wordRepository;
        this.usedWordRepository = usedWordRepository;
        this.gameRepository = gameRepository;
    }

    public void processMessage(GameMessageDto message) {
        String destination = "/topic/games/" + message.getGameId();
        messagingTemplate.convertAndSend(destination, message);
    }

    public void sendSystemMessage(Long gameId, String content) {
        GameMessageDto message = GameMessageDto.builder()
                .type("SYSTEM")
                .content(content)
                .sender("server")
                .gameId(gameId)
                .build();

        messagingTemplate.convertAndSend("/topic/games/" + gameId, message);
    }

    public void handleTurn(Long gameId, Long playerId, String wordStr) {
        System.out.println("📨 [handleTurn] gameId=" + gameId + ", playerId=" + playerId + ", word=" + wordStr);

        Game game = gameService.findGameOrThrowNotFound(gameId);

        if (!game.getCurrentTurnPlayer().getId().equals(playerId)) {
            throw new IllegalArgumentException("❌ No es tu turno");
        }

        Optional<Word> wordOpt = wordRepository.findByWordAndCollectionId(wordStr, game.getCollection().getId());
        if (wordOpt.isEmpty()) {
            throw new IllegalArgumentException("❌ La palabra no está en la colección");
        }

        Word word = wordOpt.get();

        boolean alreadyUsed = usedWordRepository.existsByGameIdAndWordId(gameId, word.getId());
        if (alreadyUsed) {
            throw new IllegalArgumentException("❌ La palabra ya fue usada");
        }

        UsedWord used = UsedWord.builder()
                .game(game)
                .word(word)
                .build();
        usedWordRepository.save(used);

        game.advanceTurn();
        gameRepository.save(game);

        messagingTemplate.convertAndSend("/topic/game/" + gameId + "/turn", wordStr);
    }

    public void startGame(Long gameId) {
        logger.info("🔁 Starting game with ID: {}", gameId);
        Game game = gameService.findGameOrThrowNotFound(gameId);
        if (game.getCurrentTurnPlayer() != null) {

            if (Math.random() < 0.5) {
                game.setCurrentTurnPlayer(game.getPlayer1());
            } else {
                game.setCurrentTurnPlayer(game.getPlayer2());
            }

            game.setGameStatus(GameStatus.ACTIVE);
            gameRepository.save(game);
            logger.info("🔁 Game started with random turn: {}", game.getCurrentTurnPlayer().getUsername());

            messagingTemplate.convertAndSend("/topic/game/" + gameId + "/start", "start");

            messagingTemplate.convertAndSend(
                    "/topic/game/" + gameId + "/turn",
                    game.getCurrentTurnPlayer().getId().toString()
            );
        }
    }
}
