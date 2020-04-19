/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.model.dao;

import com.dreamservice.model.dao.models.Executor;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

/**
 * Filters for dao
 */
public class FilteredExecutorDaoImpl implements FilteredExecutorDao {
    private final Dao<Executor> executorDao;
    private Predicate<Executor> alwaysTrue = (task) -> true;
    private Predicate<Executor> filter = alwaysTrue;


    public FilteredExecutorDaoImpl(Dao<Executor> dao) {
        this.executorDao = dao;
    }

    @Override
    public FilteredExecutorDao online(boolean isOnline) {
         filter = filter.and(executor -> {
             if (isOnline) {
                 return executor.getIsOnline();
             } else {
                 return true;
             }
        });
         return this;
    }

    @Override
    public FilteredExecutorDao newcomers(boolean isNewcomers) {
        filter = filter.and(executor -> {
            if (isNewcomers) {
                return executor.getIsNewcomer();
            } else {
                return true;
            }
        });
        return this;
    }

    @Override
    public FilteredExecutorDao withCategory(String category) {
        filter = filter.and(executor -> {
            if ("all".equals(category.toLowerCase())) return true;
            if (executor.getCategory() != null) {
                if (executor.getCategory().getName() != null) {
                    return category.toLowerCase().equals(executor.getCategory().getName().toLowerCase());
                }
            }
            return false;
        });

        return this;
    }

    @Override
    public Optional<Executor> findById(Long id) {
        return executorDao.findById(id);
    }

    @Override
    public List<Executor> findAll() {
        List<Executor> tasks = executorDao.findAll().stream()
                .filter(filter)
                .collect(toList());
        filter = alwaysTrue;
        return tasks;
    }

    @Override
    public Long create(Executor object) {
        return executorDao.create(object);
    }

    @Override
    public boolean update(Executor object) {
        return executorDao.update(object);
    }

    @Override
    public boolean delete(Executor object) {
        return executorDao.delete(object);
    }

    @Override
    public Optional<Exception> getException() {
        return executorDao.getException();
    }
}
