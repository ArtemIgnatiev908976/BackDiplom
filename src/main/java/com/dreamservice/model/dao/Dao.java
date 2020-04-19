package com.dreamservice.model.dao;

import java.util.List;
import java.util.Optional;

/**
 * CRUD operations dao
 */
public interface Dao<T> {

    /**
     *
     * @param id unique
     * @return Optional of persist object
     */
    Optional<T> findById(Long id);

    /**
     *
     * @return all objects from persist layer
     */
    List<T> findAll();

    /**
     * to Persist entity
     * @param object to persist
     * @return ID
     */
    Long create(T object);

    /**
     * @param object to update
     * @return true if was successfully updated
     */
    boolean update(T object);
    /**
     *
     * @return true if was successfully deleted
     */
    boolean delete(T object);

    /**
     * Get possible exception which was thrown during transaction
     * @return exception of transaction
     */
    Optional<Exception> getException();
}
