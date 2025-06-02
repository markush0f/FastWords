package com.fastwords.fastwords.util;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.stereotype.Component;

import com.fastwords.fastwords.shared.ResourceNotFoundException;

@Component
public class ResourceFinder {

    public <T> T findOrThrow(Supplier<Optional<T>> supplier, String resourceName, Object id) {
        return supplier.get().orElseThrow(() -> new ResourceNotFoundException(resourceName, id));
    }
}
