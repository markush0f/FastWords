package com.fastwords.fastwords.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.fastwords.fastwords.common.enums.GameStatus;

@Entity
@Table(name = "games")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "player1_id", nullable = false)
    private User player1;

    @ManyToOne
    @JoinColumn(name = "player2_id", nullable = false)
    private User player2;

    @ManyToOne
    @JoinColumn(name = "collection_id", nullable = false)
    private Collection collection;

    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private User winner;

    @ManyToOne
    @JoinColumn(name = "current_turn_player_id")
    private User currentTurnPlayer;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "time_per_turn", nullable = false)
    private Integer timePerTurn;

    public void advanceTurn() {
        if (player1 != null && player2 != null) {
            if (currentTurnPlayer.getId().equals(player1.getId())) {
                currentTurnPlayer = player2;
            } else {
                currentTurnPlayer = player1;
            }
        }
    }

    public void startGameWithRandomTurn() {
        if (player1 == null || player2 == null) {
            throw new IllegalStateException("Ambos jugadores deben estar asignados antes de iniciar el juego.");
        }

        this.currentTurnPlayer = Math.random() < 0.5 ? player1 : player2;
        this.gameStatus = GameStatus.ACTIVE;
    }

}
