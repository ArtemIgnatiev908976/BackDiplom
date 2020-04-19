/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.model.dao;

import com.dreamservice.model.dao.models.Executor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * For test needs
 */
public class MockExecutorDao implements Dao<Executor> {

    private List<Executor> allExecutors = new ArrayList<>();

    @Override
    public Optional<Executor> findById(Long id) {
        return allExecutors.stream().filter(task -> task.getId().equals(id)).findFirst();
    }

    @Override
    public List<Executor> findAll() {
        return allExecutors;
    }

    @Override
    public Long create(Executor object) {
        long id = UUID.randomUUID().getMostSignificantBits();
        allExecutors.add(object.setId(id));
        return id;
    }

    @Override
    public boolean update(Executor object) {
        Optional<Executor> first = allExecutors.stream().filter(executor -> executor.getId().equals(object.getId())).findFirst();
        boolean present = first.isPresent();
        if (present) {
            int index = allExecutors.lastIndexOf(first.get());
            allExecutors.set(index, object);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delete(Executor object) {
       return allExecutors.remove(object);
    }

    @Override
    public Optional<Exception> getException() {
        return Optional.empty();
    }
}
