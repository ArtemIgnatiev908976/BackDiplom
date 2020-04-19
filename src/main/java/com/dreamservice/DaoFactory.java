/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice;

import com.dreamservice.controllers.DaoController;
import com.dreamservice.model.dao.FilteredExecutorDaoImpl;
import com.dreamservice.model.dao.FilteredTaskDaoImpl;
import com.dreamservice.model.dao.MockDao;
import com.dreamservice.model.dao.PersonDaoImpl;

/**
 * @Author Nikita Salomatin
 * @Date 25.12.2017
 * @contact nsalomatin@hotmail.com
 */
public class DaoFactory {
    public static DaoController getDaoController(String profile) {
        if ("testing".equals(profile)) {
            return getMockedController();
        } else {
            throw new IllegalStateException("Production profile is not done yet");
        }
    }

    private static DaoController getMockedController() {
        DaoController daoController = DaoController.getInstance();
        daoController.setTaskDao(new FilteredTaskDaoImpl(new MockDao<>()))
                .setExecutorDao(new FilteredExecutorDaoImpl(new MockDao<>()))
                .setSessionDao(new MockDao<>())
                .setAuthDao(new MockDao<>())
                .setCustomerDao(new MockDao<>())
                .setPersonDao(new PersonDaoImpl(new MockDao<>(), daoController.getSessionDao(), daoController.getAuthDao()));

        return daoController;
    }
}

