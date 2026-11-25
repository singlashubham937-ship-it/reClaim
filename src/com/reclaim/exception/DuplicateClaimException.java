package com.reclaim.exception;

/**
 * Thrown when an active claim already exists for the same student and item.
 */
public class DuplicateClaimException extends DuplicateItemException {
    public DuplicateClaimException(String message) {
        super(message);
    }
}
