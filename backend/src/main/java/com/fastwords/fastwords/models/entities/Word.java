package com.fastwords.fastwords.models.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "word", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"word", "collection_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String word;

    @ManyToOne
    @JoinColumn(name = "collection_id", nullable = false)
    private Collection collection;
}