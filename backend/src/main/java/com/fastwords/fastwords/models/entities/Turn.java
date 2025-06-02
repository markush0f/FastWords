package com.fastwords.fastwords.models.entities;


import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "turns")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Turn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String word;

    @Column(name = "is_valid", nullable = false)
    private Boolean isValid;

    @Column(name = "response_time", nullable = false)
    private BigDecimal responseTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
