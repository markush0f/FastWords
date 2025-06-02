package com.fastwords.fastwords.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.fastwords.fastwords.services.CollectionServiceImpl;

@RestController
@RequestMapping("/api/v1/collection")
public class CollectionControler {

    private final CollectionService collectionService;
    private static final Logger logger = LoggerFactory.getLogger(CollectionServiceImpl.class);

    public CollectionControler(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @PostMapping("")
    public ResponseEntity<CollectionResponseDto> createCollection(@RequestBody CreateCollectionDto createCollectionDto) {
        logger.info("Creating collection with name: {}", createCollectionDto.getName());
        CollectionResponseDto collectionResponseDto = collectionService.createCollection(createCollectionDto);

        return ResponseEntity
                .created(URI.create("/" + collectionResponseDto.getId()))
                .body(collectionResponseDto);
    }

    @GetMapping("/{collectionId}")
    public ResponseEntity<CollectionResponseDto> getCollectionById(@PathVariable Long collectionId) {
        logger.info("Fetching collection with ID: {}", collectionId);
        CollectionResponseDto collectionResponseDto = collectionService.findCollectionbyId(collectionId);
        return ResponseEntity.ok(collectionResponseDto);
    }

    // @GetMapping("/{collectionName}")
    // public ResponseEntity<CollectionResponseDto> getCollectionByName(@RequestParam String collectionName) {
    //     return ResponseEntity.ok(collectionService.findCollectionByName(collectionName));
    // }
}
