/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.model.dao;

import com.dreamservice.model.dao.models.Task;

/**
 * Filters
 */
public interface FilteredTaskDao extends Dao<Task> {
    FilteredTaskDao withStatus(Status status);
    FilteredTaskDao withCategory(String category);

}
