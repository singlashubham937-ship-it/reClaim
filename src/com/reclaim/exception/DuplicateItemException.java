package com.reclaim.exception;

/**
 * Thrown when an operation would create an unauthorized or invalid duplicate entity.
 */
public class DuplicateItemException extends RuntimeException {
    public DuplicateItemException(String message) {
        super(message);
    }
}
