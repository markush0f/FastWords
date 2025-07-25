package com.fastwords.fastwords.services;

import org.springframework.stereotype.Service;

import com.fastwords.fastwords.models.dtos.CollectionResponseDto;
import com.fastwords.fastwords.models.dtos.CreateCollectionDto;
import com.fastwords.fastwords.models.entities.Collection;
import com.fastwords.fastwords.repository.CollectionRepository;

@Service
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;

    public CollectionServiceImpl(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    @Override
    public CollectionResponseDto createCollection(CreateCollectionDto createCollectionDto) {
        Collection collection = Collection.builder()
                .name(createCollectionDto.getName())
                .build();

        Collection savedCollection = collectionRepository.save(collection);

        return CollectionResponseDto.builder()
                .id(savedCollection.getId())
                .name(savedCollection.getName())
                .build();
    }

    @Override
    public CollectionResponseDto findCollectionbyId(Long collectionId) {
        Collection collection = findCollectionOrThrowNotFound(collectionId);

        return CollectionResponseDto.builder()
                .id(collection.getId())
                .name(collection.getName())
                .build(); 
    }

    @Override
    public Collection findCollectionOrThrowNotFound(Long collectionId) {
        return collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Collection not found with id: " + collectionId));
    }

    public Collection findCollectionbyName(String name) {
        return collectionRepository.findByName(name)
                .orElse(null);
    }

}
