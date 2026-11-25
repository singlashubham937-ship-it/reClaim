package model;

/**
 * Defines the possible roles a user can have in the reClaim application.
 * Used for role-based access control (e.g., restricting admin dashboard to ADMIN).
 */
public enum UserRole {
    STUDENT,   // A student who can browse items and submit claims
    FINDER,    // A user who found an item and reported it
    ADMIN      // An administrator who manages the whole system
}
