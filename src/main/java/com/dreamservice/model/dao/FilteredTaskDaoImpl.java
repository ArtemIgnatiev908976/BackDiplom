/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.model.dao;

import com.dreamservice.model.dao.models.Task;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

/**
 * Filters for dao
 */
public class FilteredTaskDaoImpl implements FilteredTaskDao {
    private final Dao<Task> taskDao;
    private Predicate<Task> alwaysTrue = (task) -> true;
    private Predicate<Task> filter = alwaysTrue;


    public FilteredTaskDaoImpl(Dao<Task> dao) {
        this.taskDao = dao;
    }


    @Override
    public FilteredTaskDao withStatus(Status status) {
        filter = filter.and((Task task) -> status.equals(Status.ANY)
                || status.equals(task.getStatusFlag()));
        return this;
    }

    @Override
    public FilteredTaskDao withCategory(String category) {
        filter = filter.and(task -> {
            if ("all".equals(category.toLowerCase())) return true;
            if (task.getCategory() != null) {
                if (task.getCategory().getName() != null) {
                    return category.toLowerCase().equals(task.getCategory().getName().toLowerCase());
                }
            }
            return false;
        });

        return this;
    }

    @Override
    public Optional<Task> findById(Long id) {
        return taskDao.findById(id);
    }

    @Override
    public List<Task> findAll() {
        List<Task> tasks = taskDao.findAll().stream()
                .filter(filter)
                .collect(toList());
        filter = alwaysTrue;
        return tasks;
    }

    @Override
    public Long create(Task object) {
        return taskDao.create(object);
    }

    @Override
    public boolean update(Task object) {
        return taskDao.update(object);
    }

    @Override
    public boolean delete(Task object) {
        return taskDao.delete(object);
    }

    @Override
    public Optional<Exception> getException() {
        return taskDao.getException();
    }
}
