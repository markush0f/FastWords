package com.fastwords.fastwords.shared;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
public class ResourceNotFoundException extends ResponseStatusException {

    public ResourceNotFoundException(String resourceName, Object id) {
        super(HttpStatus.NOT_FOUND, resourceName + " with ID " + id + " not found");
    }
}