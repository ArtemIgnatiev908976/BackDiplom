/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.model.dao;

import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * TaskHibernateDao
 */
public class HibernateDao<T extends HasId> implements Dao<T> {

    private final Class<T> clazz;
    private Exception lastException = null;

    private final EntityManager manager;
//            = Persistence
//            .createEntityManagerFactory("MyPersist")
//            .createEntityManager();


    public HibernateDao(final EntityManager manager, final Class<T> clazz) {
        this.clazz = clazz;
        this.manager = manager;
    }

    @Override
    public Optional<T> findById(Long id) {
        T entity = manager.find(clazz, id);
        return Optional.ofNullable(entity);
    }

    @Override
    public List<T> findAll() {
        TypedQuery<T> findAll = manager.createNamedQuery(clazz.getAnnotation(NamedQuery.class).name(), clazz);
        return findAll.getResultList();
    }

    @Override
    public Long create(T object) {
        try {
            manager.getTransaction().begin();
            manager.persist(object);
            manager.getTransaction().commit();
            return object.getId();
        } catch (Exception e) {
            manager.getTransaction().rollback();
            lastException = e;
            throw e;
        }
    }

    @Override
    public boolean update(T object) {
        try {
            Optional<T> possibleEntity = findById(object.getId());
            if (!possibleEntity.isPresent()) return false;
            manager.getTransaction().begin();
            manager.merge(object);
            manager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            manager.getTransaction().rollback();
            lastException = e;
            return false;
        }
    }

    @Override
    public boolean delete(T object) {
        try {
            manager.getTransaction().begin();
            manager.remove(object);
            manager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            manager.getTransaction().rollback();
            lastException = e;
            return false;
        }
    }

    @Override
    public Optional<Exception> getException() {
        return Optional.ofNullable(lastException);
    }
}
