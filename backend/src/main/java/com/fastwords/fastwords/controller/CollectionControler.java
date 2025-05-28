package com.fastwords.fastwords.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fastwords.fastwords.models.dtos.CollectionResponseDto;
import com.fastwords.fastwords.models.dtos.CreateCollectionDto;
import com.fastwords.fastwords.services.CollectionService;


@RestController
@RequestMapping("/api/v1/game")
public class CollectionControler {

    private final CollectionService collectionService;

    public CollectionControler(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @PostMapping("")
    public ResponseEntity<CollectionResponseDto> postMethodName(@RequestBody CreateCollectionDto createCollectionDto) {
        CollectionResponseDto collectionResponseDto = collectionService.createCollection(createCollectionDto);
        return ResponseEntity
                .created(URI.create("/" + collectionResponseDto.getId())) 
                .body(collectionService.createCollection(createCollectionDto));
    }

    @GetMapping("/{collectionId}")
    public ResponseEntity<CollectionResponseDto> getCollectionById(@PathVariable Long collectionId) {
        CollectionResponseDto collectionResponseDto = collectionService.findCollectionbyId(collectionId);
        return ResponseEntity.ok(collectionResponseDto);
    }
    
}
