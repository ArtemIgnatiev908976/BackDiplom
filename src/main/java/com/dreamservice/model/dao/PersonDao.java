/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.model.dao;

import com.dreamservice.model.dao.models.Person;

/**
 * @Author Nikita Salomatin
 * @Date 23.12.2017
 * @contact nsalomatin@hotmail.com
 */
public interface PersonDao extends Dao<Person> {
    Person findBySessionId(Long sessionId);
}
