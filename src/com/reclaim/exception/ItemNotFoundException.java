package com.reclaim.exception;

/**
 * Thrown when an item is not found in the repository.
 */
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(String message) {
        super(message);
    }
}
