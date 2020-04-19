/*
 * Copyright (c) 2018.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice;

import com.dreamservice.controllers.Application;
import com.dreamservice.controllers.DaoController;
import com.dreamservice.model.dao.*;
import com.dreamservice.model.dao.models.*;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * @Author Nikita Salomatin
 * @Date 24.12.2017
 * @contact nsalomatin@hotmail.com
 */
public class BackEndTest {

    @BeforeClass
    public static void beforeOnce()throws Exception {
       Application.main(new String[]{});
    }

    @After
    public void restartDao() throws Exception {
        DaoController.setInstance(DaoFactory.getDaoController("testing"));
    }

    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI = "http://localhost:8080";
     }

    protected Long createUser(String login, String password) throws UserDoesNotExistException {

        DaoController daoController = DaoController.getInstance();
        Long personId = daoController.getPersonDao().create(new Person().setFirstName(login));
        Long userId = daoController.getAuthDao()
                .create(new Auth()
                        .setEmail(login)
                        .setPassword(password)
                        .setPersonId(personId));

        Auth auth = new CheckedByExistUser(daoController.getAuthDao(), new LoginPasswordComparison(login, password)).get();
        Session session = new ReusableSession(auth, daoController.getSessionDao()).create();
        return session.getId();
    }

    protected Long createTask(Task task, Long sessionId) {
        DaoController daoController = DaoController.getInstance();
        FilteredTaskDao taskDao = daoController.getTaskDao();
        Person person = daoController.getPersonDao().findBySessionId(sessionId);
        return taskDao.create(task.setPerson(person));
    }

    protected Long createExecutor(Long taskExecutorSessionId) {
        DaoController daoController = DaoController.getInstance();
        PersonDao personDao = daoController.getPersonDao();
        FilteredExecutorDao executorDao = daoController.getExecutorDao();
        Person personFromExecutor = personDao.findBySessionId(taskExecutorSessionId);
        return executorDao.create(new Executor().setPerson(personFromExecutor));
    }

    protected Person getPersonBySessionId(Long taskExecutorSessionId) {
        DaoController daoController = DaoController.getInstance();
        PersonDao personDao = daoController.getPersonDao();
        return personDao.findBySessionId(taskExecutorSessionId);
    }

    protected ValidatableResponse addRespondByRest(String taskId, String executorSessionId) throws URISyntaxException {
        return RestAssured
                .given()
                .queryParam("taskId", taskId)
                .queryParam("sessionId", executorSessionId)
                .when().log().everything(true)
                .post(new URI("/task/add/respond"))
                .then();
    }

    protected ValidatableResponse createUserByRest(String login, String password) throws URISyntaxException {
        return RestAssured
                .given()
                .queryParam("login", login)
                .queryParam("password", password)
                .when().log().everything(true)
                .post(new URI("auth/signup"))
                .then();
    }

    protected ValidatableResponse createUserByRest(Auth auth, Person person) throws URISyntaxException {
        return RestAssured
                .given()
                .queryParam("login", auth.getEmail())
                .queryParam("password", auth.getPassword())
                .queryParam("firstName", person.getFirstName())
                .queryParam("phone", person.getPhone())
                .when().log().everything(true)
                .post(new URI("auth/signup"))
                .then();
    }

    protected ValidatableResponse showRespondsByTask(String taskId, String sessionId) throws URISyntaxException {
        return RestAssured
                .given()
                .queryParam("taskId", taskId)
                .queryParam("sessionId", sessionId)
                .when().log().everything(true)
                .get(new URI("/task/show/responds"))
                .then();
    }

    protected ValidatableResponse createTaskByRest(String taskJson, String sessionId) throws URISyntaxException {
        return RestAssured
                .given()
                .body(taskJson)
                .queryParam("sessionId", sessionId)
                .when().log().everything(true)
                .post(new URI("/task"))
                .then();
    }

    protected ValidatableResponse chooseExecutorByRest(String taskId, String executorId, String sessionId) throws URISyntaxException {
        return RestAssured
                .given()
                .queryParam("taskId", taskId)
                .queryParam("executorId", executorId)
                .queryParam("sessionId", sessionId)
                .when().log().everything(true)
                .post(new URI("/task/add/executor"))
                .then();
    }
}
