package com.fastwords.fastwords.services;

import com.fastwords.fastwords.models.entities.Collection;
import com.fastwords.fastwords.repository.CollectionRepository;
import org.springframework.stereotype.Service;

@Service
public class CollectionServiceImpl implements CollectionService {

private final CollectionRepository collectionRepository;

    public CollectionServiceImpl(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    @Override
    public Collection findCollectionOrThrowNotFound(Long collectionId) {
        return collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Collection not found with id: " + collectionId));
    }
    
}
