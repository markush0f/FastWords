package com.fastwords.fastwords.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "used_words", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"game_id", "word_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsedWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;
}