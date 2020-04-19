/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.controllers;

import com.dreamservice.model.dao.*;
import com.dreamservice.model.dao.models.Auth;
import com.dreamservice.model.dao.models.Customer;
import com.dreamservice.model.dao.models.Session;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class DaoController {

    private static DaoController instance = new DaoController();
    private FilteredTaskDao taskDao;
    private FilteredExecutorDao executorDao;
    private Dao<Auth> authDao;
    private Dao<Session> sessionDao;
    private Dao<Customer> customerDao;
    private PersonDao personDao;

    private DaoController() {

    }

    public static DaoController getInstance() {
        return instance;
    }


    public static DaoController setInstance(DaoController controller) {
         instance = controller;
         return instance;
    }
}
