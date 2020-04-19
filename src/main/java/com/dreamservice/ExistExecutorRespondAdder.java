/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice;

import com.dreamservice.controllers.DaoController;
import com.dreamservice.model.dao.FilteredExecutorDao;
import com.dreamservice.model.dao.FilteredTaskDao;
import com.dreamservice.model.dao.PersonDao;
import com.dreamservice.model.dao.models.Executor;
import com.dreamservice.model.dao.models.Person;
import com.dreamservice.model.dao.models.Task;

import java.util.Optional;

/**
 * @Author Nikita Salomatin
 * @Date 26.12.2017
 * @contact nsalomatin@hotmail.com
 */
public class ExistExecutorRespondAdder {
    private final Task task;
    private final DaoController daoController;
    private final Long sessionId;

    public ExistExecutorRespondAdder(Task task, DaoController daoController, Long sessionId) {
        this.task = task;
        this.daoController = daoController;
        this.sessionId = sessionId;
    }

    public void addRespondToTask() {
        PersonDao personDao = daoController.getPersonDao();
        Person person = personDao.findBySessionId(sessionId);
        FilteredExecutorDao executorDao = daoController.getExecutorDao();
        FilteredTaskDao taskDao = daoController.getTaskDao();

        Optional<Executor> foundExecutor = executorDao.findAll()
                .stream()
                .filter(executor -> executor.getPerson().getId().equals(person.getId()))
                .findFirst();
        if (foundExecutor.isPresent()) {
            task.addRespond(foundExecutor.get());
        } else {
            Executor executorToCreate = new Executor().setPerson(person);
            Long executorId = executorDao.create(executorToCreate);
            executorDao.update(executorToCreate.setStringId(executorId.toString()));
            task.addRespond(executorToCreate);
        }

        boolean isUpdateSuccessful = taskDao.update(task);
        if(!isUpdateSuccessful) throw new IllegalStateException("Update was not successful");

    }
}
