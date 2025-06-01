package com.fastwords.fastwords.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastwords.fastwords.models.dtos.CreateWordDto;
import com.fastwords.fastwords.models.dtos.WordResponseDto;
import com.fastwords.fastwords.services.WordService;

@RestController
@RequestMapping("/api/v1/word")
public class WordController {

    private final WordService wordService;

    public WordController(WordService wordService) {
        this.wordService = wordService;
    }

    @PostMapping("")
    public ResponseEntity<WordResponseDto> createWord(@RequestBody CreateWordDto createWordDto) {
        WordResponseDto word = wordService.createWord(createWordDto.getWord(), createWordDto.getCollectionId());
        return ResponseEntity
                .created(URI.create("/" + word.getId()))
                .body(word);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WordResponseDto> getWordById(@PathVariable Long id) {
        return ResponseEntity.ok(wordService.findWordById(id));
    }

    @GetMapping("collection/{collectionId}")
    public ResponseEntity<WordResponseDto[]> getAllWordsByCollectionId(@PathVariable Long collectionId) {
        return ResponseEntity.ok(wordService.getAllWordsByCollectionId(collectionId));
    }

}
