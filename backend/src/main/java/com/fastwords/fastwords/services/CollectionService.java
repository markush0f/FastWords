package com.fastwords.fastwords.services;

import com.fastwords.fastwords.models.dtos.CollectionResponseDto;
import com.fastwords.fastwords.models.dtos.CreateCollectionDto;
import com.fastwords.fastwords.models.entities.Collection;

public interface CollectionService {

    CollectionResponseDto createCollection(CreateCollectionDto createCollectionDto);

    Collection findCollectionOrThrowNotFound(Long collectionId);

    CollectionResponseDto findCollectionbyId(Long id);
}
