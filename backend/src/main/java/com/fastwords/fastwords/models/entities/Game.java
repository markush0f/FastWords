package com.fastwords.fastwords.models.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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
    private GameStatus status;

    @ManyToOne
    @JoinColumn(name = "winner_id")
    private User winner;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}