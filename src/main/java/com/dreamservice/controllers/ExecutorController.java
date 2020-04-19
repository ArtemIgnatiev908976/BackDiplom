/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.controllers;

import com.dreamservice.CheckedOnExistSession;
import com.dreamservice.model.dao.models.Executor;
import com.dreamservice.model.dao.models.ResultObject;
import com.dreamservice.model.dao.FilteredExecutorDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.maven.wagon.authorization.AuthorizationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class ExecutorController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping(method = GET, path = "/executors")
    public ResultObject getExecutors(
            @RequestParam(value="isOnline", defaultValue = "false")Boolean isOnline,
            @RequestParam(value="isNewcomers", defaultValue = "false")Boolean isNewcomers,
            @RequestParam(value="category", defaultValue = "ALL") String category,
            @RequestParam(value = "sessionId", defaultValue = "-1") Long sessionId) throws AuthorizationException {

        new CheckedOnExistSession(sessionId).assertExist();
        return new ResultObject()
                .withExecutors(DaoController.getInstance().getExecutorDao()
                        .online(isOnline)
                        .newcomers(isNewcomers)
                        .withCategory(category)
                        .findAll());
    }

    @RequestMapping(method = POST, path = "/executor")
    public Executor postExecutors(@RequestBody String json,
                                  @RequestParam(value = "sessionId", defaultValue = "-1") Long sessionId) throws IOException, AuthorizationException {

        new CheckedOnExistSession(sessionId).assertExist();
        Executor executor = objectMapper.readValue(json, Executor.class);
        FilteredExecutorDao executorDao = DaoController.getInstance().getExecutorDao();
        Long id = executorDao.create(executor);
        return executor.setId(id);
    }

    @RequestMapping(method = GET, path = "/executor")
    public ResponseEntity getExecutor(@RequestParam String id,
                                      @RequestParam(value = "sessionId", defaultValue = "-1") Long sessionId) throws IOException, AuthorizationException {

        new CheckedOnExistSession(sessionId).assertExist();
        FilteredExecutorDao executorDao = DaoController.getInstance().getExecutorDao();
        Optional<Executor> executor = executorDao.findById(Long.valueOf(id));

        return executor.
                <ResponseEntity>map(Executor1 -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(Executor1))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body("Executor was not found"));

    }

    @RequestMapping(method = PUT, path = "/executor")
    public ResponseEntity updateExecutor(@RequestBody String json,
                                         @RequestParam(value = "sessionId", defaultValue = "-1") Long sessionId) throws IOException, AuthorizationException {

        new CheckedOnExistSession(sessionId).assertExist();
        FilteredExecutorDao executorDao = DaoController.getInstance().getExecutorDao();
        Executor updatedExecutor = objectMapper.readValue(json, Executor.class);
        boolean isSuccess = executorDao.update(updatedExecutor);
        if (isSuccess) {
            return ResponseEntity.status(HttpStatus.OK).body(updatedExecutor);
        } else {
           return ResponseEntity.status(HttpStatus.NOT_FOUND)
                   .body(executorDao.getException()
                           .orElse(new RuntimeException("Executor not found")).getMessage());
        }

    }
}
