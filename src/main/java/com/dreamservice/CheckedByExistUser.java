/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice;

import com.dreamservice.model.dao.Dao;
import com.dreamservice.model.dao.models.Auth;

import java.util.function.Predicate;

/**
 * @Author Nikita Salomatin
 * @Date 19.12.2017
 * @contact nsalomatin@hotmail.com
 */
public final class CheckedByExistUser {

    private final Dao<Auth> authDao;
    private final Predicate<Auth> toEqual;

    public CheckedByExistUser(Dao<Auth> authDao, Predicate<Auth> toEqual) {
        this.authDao = authDao;
        this.toEqual = toEqual;
    }


    public Auth get() throws UserDoesNotExistException {
        for (Auth existAuth : authDao.findAll()) {
            if (toEqual.test(existAuth)) {
                return existAuth;
            }
        }
        throw new UserDoesNotExistException("User does not exist");
    }
}
