/*
 * Copyright (c) 2018.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */
package com.dreamservice.controllers;

import com.dreamservice.BackEndTest;
import com.dreamservice.model.dao.FilteredExecutorDao;
import com.dreamservice.model.dao.FilteredTaskDao;
import com.dreamservice.model.dao.Status;
import com.dreamservice.model.dao.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.validateMockitoUsage;

@RunWith(SpringRunner.class)
public class NewTests extends BackEndTest {

    @After
    public void validate() {
        validateMockitoUsage();
    }

    @Test
    public void signUpShouldGetFirstNameAndPhone() throws Exception {
        //GIVEN
        //WHEN
        Person person = new Person().setFirstName("nikita").setPhone("+897587");
        Auth auth = new Auth().setEmail("nsa@hotmail.com").setPassword("1234");
        Session taskOwnerSession = createUserByRest(auth, person).extract().body().as(Session.class);
        Person personFromDao = DaoController.getInstance().getPersonDao().findBySessionId(taskOwnerSession.getId());
        //THEN
        assertNotNull("Person should not be null", person);
        assertThat(personFromDao.getFirstName(), is("nikita"));
        assertThat(personFromDao.getPhone(), is("+897587"));
    }

    @Test
    public void anotherViewTrueIfUserIsNotEqualTaskPersonId() throws Exception {

        Long sessionId = createUser("nsa", "1234");
        Long sessionId2 = createUser("nsa2", "1234");

        Long taskId = createTask(new Task().setName("from nsa"), sessionId);

        AnotherView isAnotherView = RestAssured
                .given()
                .parameter("taskId", taskId.toString())
                .parameter("sessionId", sessionId2)
                .when().log().everything(true)
                .get(new URI("/task/view/isAnother"))
                .then().log().all()
                .extract()
                .as(AnotherView.class);

        assertEquals("Another view false when persons are different, should be true",
                true,
                isAnotherView.getAnotherView());

    }


    @Test
    public void anotherViewFalseIfUserIsNotEqualTaskPersonId() throws Exception {

        Long sessionId = createUser("nsa", "1234");
        Long sessionId2 = createUser("nsa2", "1234");

        Long taskId = createTask(new Task().setName("from nsa"), sessionId);

        AnotherView isAnotherView = RestAssured
                .given()
                .parameter("taskId", taskId.toString())
                .parameter("sessionId", sessionId.toString())
                .when().log().everything(true)
                .get(new URI("/task/view/isAnother"))
                .then().log().all()
                .extract()
                .as(AnotherView.class);

        assertEquals("Another view true when persons are equal, should be false",
                false,
                isAnotherView.getAnotherView());

    }

    @Test
    public void addRespondToTaskPositiveWithNewExecutor() throws Exception {

        FilteredExecutorDao executorDao = DaoController.getInstance().getExecutorDao();
        assertThat("Should not exist any executors before test", executorDao.findAll().isEmpty(), Matchers.is(true));

        Long taskOwnerSessionId = createUser("nsa", "1234");
        Long taskExecutorSessionId = createUser("nsa2", "1234");

        Long taskId = createTask(new Task().setName("from nsa"), taskOwnerSessionId);

        addRespondByRest(taskId.toString(), taskExecutorSessionId.toString()).statusCode(200);

        FilteredTaskDao taskDao = DaoController.getInstance().getTaskDao();
        Optional<Task> taskOptional = taskDao.findById(taskId);
        Set<Executor> respondExecutors = taskOptional.get().getRespondExecutors();

        Optional<Executor> first = respondExecutors.stream().findFirst();
        Person bySessionId = getPersonBySessionId(taskExecutorSessionId);
        Person personFromExecutor = first.get().getPerson();

        assertEquals("Respond was not added to task", personFromExecutor, bySessionId);
        assertEquals("Respond was not added to executor dao", executorDao.findAll().size(), 1);

    }


    @Test
    public void addRespondToTaskPositiveWithExistExecutor() throws Exception {
//GIVEN
        Long taskOwnerSessionId = createUser("nsa", "1234");
        Long taskExecutorSessionId = createUser("nsa2", "1234");

        Long taskId = createTask(new Task().setName("from nsa"), taskOwnerSessionId);
        createExecutor(taskExecutorSessionId);

        DaoController daoController = DaoController.getInstance();
        FilteredExecutorDao executorDao = daoController.getExecutorDao();
        assertThat("Should have 1 executor before test", executorDao.findAll().size(), Matchers.is(1));


//WHEN
        addRespondByRest(taskId.toString(), taskExecutorSessionId.toString()).statusCode(200);


        Optional<Task> taskOptional = daoController.getTaskDao().findById(taskId);
        Set<Executor> respondExecutors = taskOptional.get().getRespondExecutors();

        Optional<Executor> first = respondExecutors.stream().findFirst();
        Person executorPersonBySessionID = getPersonBySessionId(taskExecutorSessionId);
        Person personFromExecutorAfterAdding = first.get().getPerson();

//THEN
        assertEquals("Respond was not added to task", personFromExecutorAfterAdding, executorPersonBySessionID);
        assertEquals("Respond was not added to executor dao", executorDao.findAll().size(), 1);

    }


    @Test
    public void addRespondForExecutorWhoAlreadyResponded() throws Exception {
//GIVEN
        Long taskOwnerSessionId = createUser("nsa", "1234");
        Long taskExecutorSessionId = createUser("nsa2", "1234");

        Long taskId = createTask(new Task().setName("from nsa"), taskOwnerSessionId);
        createExecutor(taskExecutorSessionId);

        DaoController daoController = DaoController.getInstance();
        FilteredExecutorDao executorDao = daoController.getExecutorDao();


//WHEN
        addRespondByRest(taskId.toString(), taskExecutorSessionId.toString()).statusCode(200);
        addRespondByRest(taskId.toString(), taskExecutorSessionId.toString()).statusCode(200);


        Optional<Task> taskOptional = daoController.getTaskDao().findById(taskId);
        Set<Executor> respondExecutors = taskOptional.get().getRespondExecutors();

        Optional<Executor> first = respondExecutors.stream().findFirst();
        Person executorPersonBySessionID = getPersonBySessionId(taskExecutorSessionId);
        Person personFromExecutorAfterAdding = first.get().getPerson();

//THEN
        assertEquals("Respond was not added to task", personFromExecutorAfterAdding, executorPersonBySessionID);
        assertEquals("Respond was not added to executor dao", executorDao.findAll().size(), 1);

    }

    @Test
    public void addRespondThrowExceptionWhenTaskOwnerEqualExecutor() throws Exception {
//GIVEN
        Long taskOwnerSessionId = createUser("nsa", "1234");
        Long taskId = createTask(new Task().setName("from nsa"), taskOwnerSessionId);

//WHEN
        String stringBody = addRespondByRest(taskId.toString(), taskOwnerSessionId.toString())
                .statusCode(500).extract().response()
                .getBody().asString();

//THEN
        String message = new JSONObject(stringBody).getString("message");
        assertThat(message, is("Executor cannot be the same person as task owner"));



    }

    @Test
    public void respondsShouldBeShownAsList() throws Exception {
//GIVEN
        Long taskOwnerSessionId = createUser("nsa", "1234");
        Long taskExecutorSessionId = createUser("nsa2", "1234");
        Long taskExecutorSessionId2 = createUser("nsa3", "1234");

        Long taskId = createTask(new Task().setName("from nsa"), taskOwnerSessionId);

        addRespondByRest(taskId.toString(), taskExecutorSessionId.toString());
        addRespondByRest(taskId.toString(), taskExecutorSessionId2.toString());

//WHEN
        String stringBody = showRespondsByTask(taskId.toString(), taskExecutorSessionId.toString()).log().all()
                .extract().body().asString();

        ObjectMapper objectMapper = new ObjectMapper();
        Set<Executor> executorSet = new HashSet<>();
        Set<Executor> set = (Set<Executor>)objectMapper.readValue(stringBody, executorSet.getClass());

//THEN
        assertThat("Size of responded executors should be 2", set.size(), is(2));

    }

    @Test
    public void executorPersonShouldUpdateWhenPersonUpdates() throws Exception {
//GIVEN
        Long taskOwnerSessionId = createUser("nsa", "1234");
        Long taskExecutorSessionId = createUser("nsa2", "1234");
//WHEN
        Long taskId = createTask(new Task().setName("from nsa"), taskOwnerSessionId);
        addRespondByRest(taskId.toString(), taskExecutorSessionId.toString());

        Person personBySessionId = getPersonBySessionId(taskExecutorSessionId);
        Task taskByRest = new Task().setName("from nsa2")
                .setPerson(personBySessionId.setFirstName("nsa2").setPhone("+71234"));

        ObjectMapper objectMapper = new ObjectMapper();
        Task taskByRest2 =
                createTaskByRest(objectMapper.writeValueAsString(taskByRest), taskExecutorSessionId.toString())
                .log().all()
                .extract().body().as(Task.class);
        Task taskAfterUpdate = DaoController.getInstance().getTaskDao().findById(taskId).get();

        Set<Executor> executorSet = new HashSet<>();
        executorSet = showRespondsByTask(taskId.toString(), taskOwnerSessionId.toString())
                .log().all().extract().body().as(executorSet.getClass());
        System.out.println("SET\n" +executorSet);
//THEN
        Executor executor = taskAfterUpdate.getRespondExecutors().iterator().next();
        assertThat("First name should be updated", executor.getPerson().getFirstName(), is("nsa2"));
        assertThat("Phone should be updated", executor.getPerson().getPhone(), is("+71234"));

    }

    @Test
    public void showRespondsFailed() throws Exception {
        //GIVEN
        Session taskOwnerSession = createUserByRest("nsa", "1234").extract().body().as(Session.class);
        Session taskExecutorSession = createUserByRest("nsa2", "1234").extract().body().as(Session.class);


        //WHEN
        ObjectMapper objectMapper = new ObjectMapper();
        String taskString = objectMapper.writeValueAsString(new Task().setName("nsa")
                .setPerson(new Person().setFirstName("Nikita").setPhone("+776587")));
        Task taskResponse = createTaskByRest(taskString, taskOwnerSession.getStringId()).log().all()
                .extract().body().as(Task.class);
        addRespondByRest(taskResponse.getStringId(), taskExecutorSession.getStringId());
        //THEN
        showRespondsByTask(taskResponse.getStringId(), taskOwnerSession.getStringId()).log().all()
                .statusCode(200);

    }

    @Test
    public void addTaskShouldCreateTaskWithStringId() throws Exception {
        //GIVEN
        Session taskOwnerSession = createUserByRest("nsa", "1234").extract().body().as(Session.class);
        //WHEN
        ObjectMapper objectMapper = new ObjectMapper();
        String taskString = objectMapper.writeValueAsString(new Task().setName("nsa")
                .setPerson(new Person().setFirstName("Nikita").setPhone("+776587")));
        Task taskResponse = createTaskByRest(taskString, taskOwnerSession.getStringId()).log().all()
                .statusCode(200)
                .extract().body().as(Task.class);

        assertNotNull(taskResponse.getStringId());
        assertThat(taskResponse.getStringId(), not(""));

    }

    @Test
    public void signUpCreateNewPerson() throws Exception {
        //GIVEN
        //WHEN
        Session taskOwnerSession = createUserByRest("nsa", "1234").extract().body().as(Session.class);
        Person person = DaoController.getInstance().getPersonDao().findBySessionId(taskOwnerSession.getId());
        //THEN
        assertNotNull("Person should not be null", person);
        assertThat(person.getFirstName(), is(""));

    }

    @Test
    public void showRespondsShouldReturnActualPerson() throws Exception {
        //GIVEN
        Session taskOwnerSession = createUserByRest("nsa", "1234").extract().body().as(Session.class);
        Session taskExecutorSession = createUserByRest("nsa2", "1234").extract().body().as(Session.class);

        //WHEN
        ObjectMapper objectMapper = new ObjectMapper();
        String taskString = objectMapper.writeValueAsString(new Task().setName("nsa")
                .setPerson(new Person().setFirstName("Nikita").setPhone("+776587")));
        Task taskResponse = createTaskByRest(taskString, taskOwnerSession.getStringId()).log().all()
                .extract().body().as(Task.class);
        addRespondByRest(taskResponse.getStringId(), taskExecutorSession.getStringId());

        String taskString2 = objectMapper.writeValueAsString(new Task().setName("nsa2")
                .setPerson(new Person().setFirstName("Nikita2").setPhone("+7765872")));
        Task taskResponse2 = createTaskByRest(taskString2, taskExecutorSession.getStringId()).log().all()
                .extract().body().as(Task.class);

        String stringExecutors = showRespondsByTask(taskResponse.getStringId(), taskOwnerSession.getStringId()).log().all()
                .statusCode(200).extract().body().asString();

        Task taskFromOwner = DaoController.getInstance().getTaskDao().findById(taskResponse.getId()).get();

        //THEN
        assertThat(taskFromOwner.getRespondExecutors().iterator().next().getPerson().getFirstName(), is("Nikita2"));

    }

    @Test
    public void addExecutorPositive() throws Exception {
        //GIVEN
        Session firstUserSession = createUserByRest("nsa", "1234").extract().body().as(Session.class);
        Session secondUserSession = createUserByRest("nsa2", "1234").extract().body().as(Session.class);

        //WHEN
        ObjectMapper objectMapper = new ObjectMapper();
        String taskJson = objectMapper.writeValueAsString(new Task().setName("nsa")
                .setPerson(new Person().setFirstName("Nikita").setPhone("+776587")));
        Task createdTask = createTaskByRest(taskJson, firstUserSession.getStringId()).extract().body().as(Task.class);
        addRespondByRest(createdTask.getStringId(), secondUserSession.getStringId());
        List<Executor> executors = DaoController.getInstance().getExecutorDao().findAll();
        Executor executor = executors.get(0);
        chooseExecutorByRest(createdTask.getStringId(), executor.getStringId(), firstUserSession.getStringId())
                .log().all().statusCode(200);
        FilteredTaskDao taskDao = DaoController.getInstance().getTaskDao();
        Task resultTask = taskDao.findById(createdTask.getId()).get();
        //THEN
        assertThat("Executor id was not added to task", resultTask.getExecutorId(), is(executor.getId()));

    }

    @Test
    public void addExecutorShouldChangeStatusToInProgress() throws Exception {
        //GIVEN
        Session firstUserSession = createUserByRest("nsa", "1234").extract().body().as(Session.class);
        Session secondUserSession = createUserByRest("nsa2", "1234").extract().body().as(Session.class);

        //WHEN
        ObjectMapper objectMapper = new ObjectMapper();
        String taskJson = objectMapper.writeValueAsString(new Task().setName("nsa")
                .setPerson(new Person().setFirstName("Nikita").setPhone("+776587")));
        Task createdTask = createTaskByRest(taskJson, firstUserSession.getStringId()).extract().body().as(Task.class);
        addRespondByRest(createdTask.getStringId(), secondUserSession.getStringId());
        List<Executor> executors = DaoController.getInstance().getExecutorDao().findAll();
        Executor executor = executors.get(0);
        chooseExecutorByRest(createdTask.getStringId(), executor.getStringId(), firstUserSession.getStringId())
                .log().all().statusCode(200);
        FilteredTaskDao taskDao = DaoController.getInstance().getTaskDao();
        Task resultTask = taskDao.findById(createdTask.getId()).get();
        //THEN
        assertThat("Status of task is not IN_PROGRESS", resultTask.getStatusFlag(), is(Status.IN_PROGRESS));

    }
}
