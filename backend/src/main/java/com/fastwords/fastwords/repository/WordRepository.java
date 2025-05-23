package com.fastwords.fastwords.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fastwords.fastwords.models.entities.Collection;
import com.fastwords.fastwords.models.entities.Word;

public interface WordRepository extends JpaRepository<Word, Long> {

    Optional<Word> findByWordAndCollection(String word, Collection collection);

    List<Word> findAllByCollection(Collection collection);
}
