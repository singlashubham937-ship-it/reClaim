package com.reclaim.exception;

/**
 * Thrown when a user attempts an action they do not have permission to perform.
 */
public class UnauthorizedActionException extends RuntimeException {
    public UnauthorizedActionException(String message) {
        super(message);
    }
}
