/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.model.dao;

/**
 * @Author Nikita Salomatin
 * @Date 19.12.2017
 * @contact nsalomatin@hotmail.com
 */
public interface SetId<T> extends HasId{
    T setId(Long id);
}
