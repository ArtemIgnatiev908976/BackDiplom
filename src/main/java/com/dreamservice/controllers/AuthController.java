/*
 * Copyright (c) 2018.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.controllers;

import com.dreamservice.*;
import com.dreamservice.model.dao.Dao;
import com.dreamservice.model.dao.PersonDao;
import com.dreamservice.model.dao.models.Auth;
import com.dreamservice.model.dao.models.Person;
import com.dreamservice.model.dao.models.Session;
import org.apache.maven.wagon.authorization.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class AuthController {

    @RequestMapping(method = {GET, OPTIONS}, path = "/auth/signin")
    @CrossOrigin(origins = {"http://localhost:63342", "https://artemignatiev908976.github.io/diplomTest", "http://localhost:5000", "http://arcane-waters-26008.herokuapp.com"})
    public ResponseEntity<?> getSessionId(@RequestParam(value = "login", defaultValue = "") String login,
                                             @RequestParam(value = "password", defaultValue = "") String password) {

        MultiValueMap<String, String> headers = new AccessHeaders().getHeaders();

        Dao<Auth> authDao = DaoController.getInstance().getAuthDao();
        Dao<Session> sessionDao = DaoController.getInstance().getSessionDao();
        try {
            Auth auth = new CheckedByExistUser(authDao, new LoginPasswordComparison(login, password)).get();
            Session session = new ReusableSession(auth, sessionDao).create();
            session.setStringId(session.getId().toString());
            return new ResponseEntity<>(session, headers, HttpStatus.OK);
        } catch (UserDoesNotExistException e) {
            return new ResponseEntity<>("You are not authorized", headers, HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(method = {POST}, path = "/auth/signup")
    @CrossOrigin(origins = {"http://localhost:63342", "https://artemignatiev908976.github.io/diplomTest", "http://localhost:5000", "http://arcane-waters-26008.herokuapp.com"})
    public ResponseEntity<?> createUser(@RequestParam(value = "login", defaultValue = "") String login,
                                        @RequestParam(value = "password", defaultValue = "") String password,
                                        @RequestParam(value = "firstName", defaultValue = "") String firstName,
                                        @RequestParam(value = "phone", defaultValue = "") String phone
                                        )
            throws EmptyCredentialException {

        MultiValueMap<String, String> headers = new AccessHeaders().getHeaders();
        Dao<Auth> authDao = DaoController.getInstance().getAuthDao();
        PersonDao personDao = DaoController.getInstance().getPersonDao();

        try {
            new CheckEmptyValues(login, password).assertEmpty();
            new CheckedByExistUser(authDao, new LoginComparison(login)).get();
            return new ResponseEntity<>("User already exist", headers, HttpStatus.FORBIDDEN);
        } catch (UserDoesNotExistException e) {
            Auth auth = new Auth()
                    .setEmail(login)
                    .setPassword(password)
                    .setPersonId(personDao.create(new Person().setEmail(login).setFirstName(firstName).setPhone(phone)));
            authDao.create(auth);
            return this.getSessionId(login, password);

        }
    }

    @RequestMapping(method = {GET}, path = "/auth/person")
    @CrossOrigin(origins = {"http://localhost:63342", "https://artemignatiev908976.github.io/diplomTest", "http://localhost:5000", "http://arcane-waters-26008.herokuapp.com"})
    public ResponseEntity<?> getPerson(@RequestParam(value = "sessionId", defaultValue = "")String sessionId)
            throws AuthorizationException {

        PersonDao personDao = DaoController.getInstance().getPersonDao();

        new CheckedOnExistSession(Long.valueOf(sessionId)).assertExist();
        Person foundPerson = personDao.findBySessionId(Long.valueOf(sessionId));

        return new ResponseEntity<>(foundPerson, new AccessHeaders().getHeaders(), HttpStatus.OK);

    }
}
