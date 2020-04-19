/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.model.dao;

import com.dreamservice.model.dao.models.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * For test needs
 */
public class MockTaskDao implements Dao<Task> {

    private List<Task> allTasks = new ArrayList<>();

    @Override
    public Optional<Task> findById(Long id) {
        return allTasks.stream().filter(task -> task.getId().equals(id)).findFirst();
    }

    @Override
    public List<Task> findAll() {
        return allTasks;
    }

    @Override
    public Long create(Task object) {
        long id = UUID.randomUUID().getMostSignificantBits();
        allTasks.add(object.setId(id));
        return id;
    }

    @Override
    public boolean update(Task object) {
        Optional<Task> first = allTasks.stream().filter(task -> task.getId().equals(object.getId())).findFirst();
        boolean present = first.isPresent();
        if (present) {
            int index = allTasks.lastIndexOf(first.get());
            allTasks.set(index, object);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delete(Task object) {
       return allTasks.remove(object);
    }

    @Override
    public Optional<Exception> getException() {
        return Optional.empty();
    }
}
