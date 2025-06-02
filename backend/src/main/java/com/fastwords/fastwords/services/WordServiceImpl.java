package com.fastwords.fastwords.services;

import org.springframework.stereotype.Service;

import com.fastwords.fastwords.models.dtos.WordResponseDto;
import com.fastwords.fastwords.models.entities.Collection;
import com.fastwords.fastwords.models.entities.Word;
import com.fastwords.fastwords.repository.WordRepository;

@Service
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;
    private final CollectionService collectionService;

    public WordServiceImpl(WordRepository wordRepository, CollectionService collectionService) {
        this.collectionService = collectionService;
        this.wordRepository = wordRepository;
    }

    @Override
    public WordResponseDto createWord(String nameWord, Long collectionId) {
        try {
            Collection collection = collectionService.findCollectionOrThrowNotFound(collectionId);
            Word word = Word.builder()
                    .word(nameWord)
                    .collection(collection)
                    .build();
                    
            if (wordRepository.findByWordAndCollectionId(word.getWord(), collectionId).isPresent()) {
                throw new RuntimeException("Word '" + word.getWord() + "' already exists in collection with id: " + collectionId);
            }

            Word savedWord = wordRepository.save(word);

            return WordResponseDto.builder()
                    .id(savedWord.getId())
                    .word(savedWord.getWord())
                    .collectionId(savedWord.getCollection().getId())
                    .build();
        } catch (RuntimeException e) {
            throw new RuntimeException("Error creating word: " + e.getMessage(), e);
        }

    }

    @Override
    public WordResponseDto findWordById(Long id) {
        try {
            Word word = wordRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Word not found with id: " + id));

            return WordResponseDto.builder()
                    .id(word.getId())
                    .word(word.getWord())
                    .collectionId(word.getCollection().getId())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error finding word by id: " + e.getMessage(), e);
        }
    }

    @Override
    public WordResponseDto[] getAllWordsByCollectionId(Long collectionId) {
        try {
            
        Collection collection = collectionService.findCollectionOrThrowNotFound(collectionId);
            return wordRepository.findAllByCollection(collection).stream()
                    .map(word -> WordResponseDto.builder()
                            .id(word.getId())
                            .word(word.getWord())
                            .collectionId(word.getCollection().getId())
                            .build())
                    .toArray(WordResponseDto[]::new);

        } catch (Exception e) {
            throw new RuntimeException("Error retrieving words by collection id: " + e.getMessage(), e);
        }
    }

}
