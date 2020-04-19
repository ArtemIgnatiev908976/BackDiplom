/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice;

import com.dreamservice.model.dao.Dao;
import com.dreamservice.model.dao.models.Auth;
import com.dreamservice.model.dao.models.Session;

import java.util.Optional;
import java.util.function.BiPredicate;

/**
 * @Author Nikita Salomatin
 * @Date 19.12.2017
 * @contact nsalomatin@hotmail.com
 */
public final class ReusableSession implements CreateSession {

    private final Dao<Session> sessionDao;
    private final Auth auth;


    public ReusableSession(Auth auth, Dao<Session> sessionDao) {
        this.auth = auth;
        this.sessionDao = sessionDao;
    }

    private BiPredicate<Session, Auth> isSessionExist = (session, auth) -> session.getAuthId().equals(auth.getId());

    @Override
    public Session create() {
        Optional<Session> result = Optional.empty();
        for (Session existSession : sessionDao.findAll()) {
            if (isSessionExist.test(existSession, auth)) {
                result = Optional.of(existSession);
            }
        }
        if(!result.isPresent()) {
            Session newSession = new Session();
            newSession.setAuthId(auth.getId());
            Long sessionId = sessionDao.create(newSession);
            newSession.setId(sessionId);
            result = Optional.of(newSession);
        }
        return result.get();
    }
}
