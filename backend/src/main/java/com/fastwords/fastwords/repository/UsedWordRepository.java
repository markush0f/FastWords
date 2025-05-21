package com.fastwords.fastwords.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastwords.fastwords.models.entities.Game;
import com.fastwords.fastwords.models.entities.UsedWord;
import com.fastwords.fastwords.models.entities.Word;

public interface UsedWordRepository extends JpaRepository<UsedWord, Long> {
    boolean existsByGameAndWord(Game game, Word word);
}