package com.reclaim.service;

import java.util.ArrayList;

/**
 * Generic search interface for entities in the reClaim system.
 *
 * @param <T> The entity type being searched.
 */
public interface Searchable<T> {

    /**
     * Searches for an entity by its unique ID.
     *
     * @param id The entity ID.
     * @return The entity if found and available, or null.
     */
    T searchById(int id);

    /**
     * Searches for entities whose name matches the given query.
     *
     * @param name The name or partial name to search for.
     * @return An ArrayList containing matching entities.
     */
    ArrayList<T> searchByName(String name);
}
