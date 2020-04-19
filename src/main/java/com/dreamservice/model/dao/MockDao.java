/*
 * Copyright (c) 2017. 
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @Author Nikita Salomatin
 * @Date 19.12.2017
 * @contact nsalomatin@hotmail.com
 */
public class MockDao<T extends SetId> implements Dao<T> {

    private List<T> allTs = new ArrayList<>();

    @Override
    public Optional<T> findById(Long id) {
        return allTs.stream().filter(T -> T.getId().equals(id)).findFirst();
    }

    @Override
    public List<T> findAll() {
        return allTs;
    }

    @Override
    public Long create(T object) {
        long id = UUID.randomUUID().getMostSignificantBits();
        object.setId(id);
        allTs.add(object);
        return id;
    }

    @Override
    public boolean update(T object) {
        Optional<T> first = allTs.stream().filter(T -> T.getId().equals(object.getId())).findFirst();
        boolean present = first.isPresent();
        if (present) {
            int index = allTs.lastIndexOf(first.get());
            allTs.set(index, object);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean delete(T object) {
        return allTs.remove(object);
    }

    @Override
    public Optional<Exception> getException() {
        return Optional.empty();
    }
}
