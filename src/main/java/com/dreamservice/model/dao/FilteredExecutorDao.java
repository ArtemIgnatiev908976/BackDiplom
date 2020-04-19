/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.model.dao;

import com.dreamservice.model.dao.models.Executor;

/**
 * Filters for executors
 */
public interface FilteredExecutorDao extends Dao<Executor> {

    /**
     * Filter by online status, result will be list of online people
     * @param isOnline - show only online
     * @return filtered dao
     */
    FilteredExecutorDao online(boolean isOnline);

    /**
     * Filter by newcomers, result will be list of people who not so time ago signed up
     * @param isNewcomers - show only newcomers
     * @return filtered dao
     */
    FilteredExecutorDao newcomers(boolean isNewcomers);

    /**
     * Filter by category, result will be list of people who work with that category
     * @param category - category an executor work with
     * @return filtered dao
     */
    FilteredExecutorDao withCategory(String category);

}
