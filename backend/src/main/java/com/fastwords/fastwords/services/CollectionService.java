package com.fastwords.fastwords.services;

import com.fastwords.fastwords.models.entities.Collection;

public interface CollectionService {

    Collection findCollectionOrThrowNotFound(Long collectionId);

}
