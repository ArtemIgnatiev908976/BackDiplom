/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.controllers;

import com.dreamservice.*;
import com.dreamservice.model.dao.FilteredTaskDao;
import com.dreamservice.model.dao.PersonDao;
import com.dreamservice.model.dao.Status;
import com.dreamservice.model.dao.models.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.maven.wagon.authorization.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class TasksController {

    private ObjectMapper objectMapper = new ObjectMapper();


    @RequestMapping(method = {GET, OPTIONS}, path = "/tasks")
    public ResponseEntity<ResultObject> getTasks(
                              @RequestParam(value="status", defaultValue = "ANY") Status status,
                              @RequestParam(value="category", defaultValue = "ALL") String category,
                              @RequestParam(value = "sessionId", defaultValue = "-1") String sessionId) throws AuthorizationException {

        new CheckedOnExistSession(Long.valueOf(sessionId)).assertExist();
        FilteredTaskDao filteredTaskDao = DaoController.getInstance().getTaskDao();
        int totalAmount = filteredTaskDao.findAll().size();

        ResultObject body = new ResultObject()
                .withTotalAmount(totalAmount)
                .withItems(filteredTaskDao
                        .withStatus(status)
                        .withCategory(category)
                        .findAll().stream().peek(setId).collect(Collectors.toList()));

        MultiValueMap<String, String> headers = new AccessHeaders().getHeaders();
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }


    private Consumer<? super Task> setId = (task) -> task.setStringId(task.getId().toString());


    @RequestMapping(method = POST, path = "/task")
    public Task postTasks(@RequestBody String json,
                          @RequestParam(value = "sessionId", defaultValue = "-1") Long sessionId)
            throws IOException, AuthorizationException {

        new CheckedOnExistSession(sessionId).assertExist();
        FilteredTaskDao filteredTaskDao = DaoController.getInstance().getTaskDao();
        PersonDao personDao = DaoController.getInstance().getPersonDao();

        Task task = objectMapper.readValue(json, Task.class);
        Person foundPerson = personDao.findBySessionId(sessionId);
        Person personFromJson = task.getPerson();
        personFromJson.setId(foundPerson.getId());
        personDao.update(personFromJson);
        task.setPerson(personFromJson);
        Long id = filteredTaskDao.create(task);
        return task.setId(id).setStringId(id.toString());
    }

    @RequestMapping(method = GET, path = "/task")
    public ResponseEntity getTask(@RequestParam String id,
                                  @RequestParam(value = "sessionId", defaultValue = "-1") Long sessionId) throws IOException, AuthorizationException {

        new CheckedOnExistSession(sessionId).assertExist();
        FilteredTaskDao taskDao = DaoController.getInstance().getTaskDao();
        PersonDao personDao = DaoController.getInstance().getPersonDao();
        Optional<Task> task = taskDao.findById(Long.valueOf(id));
        task.ifPresent(task1 -> {
            Person person = task1.getPerson();
            Optional<Person> foundFromRepository = personDao.findById(person.getId());
            foundFromRepository.ifPresent(person1 -> taskDao.update(task1.setPerson(person1)));
        });

        return task.
                <ResponseEntity>map(task1 -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(task1))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Task was not found"));

    }

    @RequestMapping(method = PUT, path = "/task")
    @CrossOrigin(origins = {"http://localhost:63342", "https://artemignatiev908976.github.io/diplomTest", "http://localhost:5000", "http://arcane-waters-26008.herokuapp.com"})
    public ResponseEntity updateTask(@RequestBody String json,
                                     @RequestParam(value = "sessionId", defaultValue = "-1") Long sessionId) throws IOException, AuthorizationException {
        new CheckedOnExistSession(sessionId).assertExist();
        FilteredTaskDao taskDao = DaoController.getInstance().getTaskDao();
        Task updatedTask = objectMapper.readValue(json, Task.class);
        boolean isSuccess = taskDao.update(updatedTask);
        PersonDao personDao = DaoController.getInstance().getPersonDao();
        personDao.update(updatedTask.getPerson());
        MultiValueMap<String, String> headers = new AccessHeaders().getHeaders();
        if (isSuccess) {
            return new ResponseEntity<>(updatedTask, headers, HttpStatus.OK);
        } else {
           return new ResponseEntity<>(taskDao
                   .getException()
                   .orElse(new RuntimeException("Task not found")), headers, HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(method = GET, path = "/task/view/isAnother")
    @CrossOrigin(origins = {"http://localhost:63342", "https://artemignatiev908976.github.io/diplomTest", "http://localhost:5000", "http://arcane-waters-26008.herokuapp.com"})
    public ResponseEntity<?> isAnotherView(@RequestParam(value = "taskId") String taskId,
                                  @RequestParam(value = "sessionId", defaultValue = "-1") String sessionId) throws IOException, AuthorizationException {

        Long lSessionId = Long.valueOf(sessionId);
        new CheckedOnExistSession(lSessionId).assertExist();

        FilteredTaskDao filteredTaskDao = DaoController.getInstance().getTaskDao();
        PersonDao personDao = DaoController.getInstance().getPersonDao();
        Task task = filteredTaskDao.findById(Long.valueOf(taskId))
                .orElseThrow(() -> new IllegalStateException("Task not found"));
        Person user = personDao.findBySessionId(lSessionId);
        boolean isAnotherView = new TaskViewChecker(task, user).isAnotherView();
        return new ResponseEntity<>(
                new AnotherView().setAnotherView(isAnotherView),
                new AccessHeaders().getHeaders(),
                HttpStatus.OK);

    }

    @RequestMapping(method = POST, path = "/task/add/respond")
//    @CrossOrigin(origins = {"http://localhost:63342"})
    public ResponseEntity addRespondOfExecutor(@RequestParam(value = "taskId", defaultValue = "-1") Long taskId,
                                               @RequestParam(value = "sessionId", defaultValue = "-1") Long sessionId)
            throws IOException, AuthorizationException, ExecutorException {

        new CheckedOnExistSession(sessionId).assertExist();
        DaoController daoController = DaoController.getInstance();
        Task task = daoController.getTaskDao().findById(taskId).orElseThrow(
                () -> new IllegalStateException("Task not found for adding executor respond"));

        new CheckedPerson(task.getPerson(), daoController).assertNotEqualTo(sessionId);
        new ExistExecutorRespondAdder(task, daoController, sessionId).addRespondToTask();
        return new ResponseEntity(new AccessHeaders().getHeaders(), HttpStatus.OK);

    }

    @RequestMapping(method = GET, path = "/task/show/responds")
//    @CrossOrigin(origins = {"http://localhost:63342"})
    public ResponseEntity<?> showRespondsByTask(@RequestParam(value = "taskId", defaultValue = "-1") Long taskId,
                                               @RequestParam(value = "sessionId", defaultValue = "-1") Long sessionId)
            throws IOException, AuthorizationException, ExecutorException {

        new CheckedOnExistSession(sessionId).assertExist();
        DaoController daoController = DaoController.getInstance();
        PersonDao personDao = daoController.getPersonDao();
        Task task = daoController.getTaskDao().findById(taskId).orElseThrow(
                () -> new IllegalStateException("Task not found for showing responds by task"));

        Iterator<Executor> iterator = task.getRespondExecutors().iterator();
        while (iterator.hasNext()) {
            Executor next = iterator.next();
            Long personId = next.getPerson().getId();
            next.setPerson(personDao.findById(personId).orElseThrow(() ->
            new IllegalStateException("Person not found while updating executors responds")));
        }

        return new ResponseEntity<>(task.getRespondExecutors(), new AccessHeaders().getHeaders(), HttpStatus.OK);

    }

    @RequestMapping(method = POST, path = "/task/add/executor")
    public ResponseEntity addRespondOfExecutor(@RequestParam(value = "taskId", defaultValue = "-1") Long taskId,
                                               @RequestParam(value = "executorId") Long executorId,
                                               @RequestParam(value = "sessionId", defaultValue = "-1") Long sessionId)
            throws IOException, AuthorizationException {

        new CheckedOnExistSession(sessionId).assertExist();
        DaoController daoController = DaoController.getInstance();
        FilteredTaskDao taskDao = daoController.getTaskDao();
        Task task = taskDao.findById(taskId).orElseThrow(
                () -> new IllegalStateException("Task not found adding executor"));

        if (task.getExecutorId() != null) {
            throw new IllegalStateException("Executor was already chosen");
        }

        taskDao.update(task.setExecutorId(executorId).setStatusFlag(Status.IN_PROGRESS));
        return new ResponseEntity(new AccessHeaders().getHeaders(), HttpStatus.OK);

    }
}
