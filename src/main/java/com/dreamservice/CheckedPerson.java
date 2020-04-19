/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice;

import com.dreamservice.controllers.DaoController;
import com.dreamservice.model.dao.models.Person;

/**
 * @Author Nikita Salomatin
 * @Date 26.12.2017
 * @contact nsalomatin@hotmail.com
 */
public class CheckedPerson {
    private final Person person;
    private final DaoController daoController;

    public CheckedPerson(Person person, DaoController daoController) {
        this.person = person;
        this.daoController = daoController;
    }

    public void assertNotEqualTo(Long executorSessionId) throws ExecutorException {
        Person executorPerson = daoController.getPersonDao().findBySessionId(executorSessionId);
        if (executorPerson.equals(person))
            throw new ExecutorException("Executor cannot be the same person as task owner");

    }
}
