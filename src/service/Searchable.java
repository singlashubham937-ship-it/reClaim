package service;

/**
 * Interface representing searchable entities in the reClaim system.
 * Provides a beginner-friendly contract for searching by name.
 */
public interface Searchable {
    /**
     * Checks if the entity matches the given name or keyword.
     * @param name The search query or name keyword.
     * @return true if the entity matches, false otherwise.
     */
    boolean searchByName(String name);
}
