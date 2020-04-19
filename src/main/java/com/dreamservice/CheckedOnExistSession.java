/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice;

import com.dreamservice.controllers.DaoController;
import com.dreamservice.model.dao.Dao;
import com.dreamservice.model.dao.models.Session;
import org.apache.maven.wagon.authorization.AuthorizationException;

import java.util.Objects;

/**
 * @Author Nikita Salomatin
 * @Date 19.12.2017
 * @contact nsalomatin@hotmail.com
 */
public class CheckedOnExistSession {
    private final Long sessionId;
    private final Dao<Session> sessionDao;

    public CheckedOnExistSession(Long sessionId) {
        this(sessionId, DaoController.getInstance().getSessionDao());
    }

    public CheckedOnExistSession(Long sessionId, Dao<Session> sessionDao) {
        Objects.requireNonNull(sessionId);
        Objects.requireNonNull(sessionDao);
        this.sessionId = sessionId;
        this.sessionDao = sessionDao;
    }

    private boolean isExist() {
       return sessionDao.findById(sessionId).isPresent();
    }

    public void assertExist() throws AuthorizationException {
        if (!isExist()) throw new AuthorizationException("You are not authorized");
    }
}
