/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.model.dao;

import com.dreamservice.model.dao.models.Auth;
import com.dreamservice.model.dao.models.Person;
import com.dreamservice.model.dao.models.Session;

import java.util.List;
import java.util.Optional;

/**
 * @Author Nikita Salomatin
 * @Date 23.12.2017
 * @contact nsalomatin@hotmail.com
 */
public class PersonDaoImpl implements PersonDao {

    private final Dao<Person> personDao;
    private final Dao<Session> sessionDao;
    private final Dao<Auth> authDao;

    public PersonDaoImpl(Dao<Person> personDao, Dao<Session> sessionDao, Dao<Auth> authDao) {
        this.personDao = personDao;
        this.sessionDao = sessionDao;
        this.authDao = authDao;
    }

    @Override
    public Person findBySessionId(Long sessionId) {

        Session session = sessionDao.findById(sessionId).orElseThrow(
                () -> new IllegalArgumentException("Session not found"));

        Auth auth = authDao.findById(session.getAuthId()).orElseThrow(
                () -> new IllegalArgumentException("Auth not found for exist session"));

        return personDao.findById(auth.getPersonId()).orElseThrow(
                () -> new IllegalArgumentException("Person not found for exist auth"));
    }

    @Override
    public Optional<Person> findById(Long id) {
        return personDao.findById(id);
    }

    @Override
    public List<Person> findAll() {
        return personDao.findAll();
    }

    @Override
    public Long create(Person object) {
        return personDao.create(object);
    }

    @Override
    public boolean update(Person object) {
        return personDao.update(object);
    }

    @Override
    public boolean delete(Person object) {
        return personDao.delete(object);
    }

    @Override
    public Optional<Exception> getException() {
        return personDao.getException();
    }
}
