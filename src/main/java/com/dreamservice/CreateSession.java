/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice;

import com.dreamservice.model.dao.models.Session;

/**
 * @Author Nikita Salomatin
 * @Date 19.12.2017
 * @contact nsalomatin@hotmail.com
 */
public interface CreateSession {
    Session create() throws Exception;
}
