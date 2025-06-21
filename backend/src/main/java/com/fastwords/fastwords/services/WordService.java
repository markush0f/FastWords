package com.fastwords.fastwords.services;

import com.fastwords.fastwords.models.dtos.WordResponseDto;

public interface WordService {
    
    WordResponseDto createWord(String word, Long collectionId);

    WordResponseDto findWordById(Long id);

    WordResponseDto[] getAllWordsByCollectionId(Long collectionId);

    boolean checkWordExists(String word, Long collectionId);
}
