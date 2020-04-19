/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice;

import com.dreamservice.model.dao.models.Person;
import com.dreamservice.model.dao.models.Task;

/**
 * @Author Nikita Salomatin
 * @Date 24.12.2017
 * @contact nsalomatin@hotmail.com
 */
public class TaskViewChecker {

    private final Task task;
    private final Person person;

    public TaskViewChecker(Task task, Person person) {
        this.task = task;
        this.person = person;
    }

    public boolean isAnotherView() {
        Long taskPersonId = task.getPerson().getId();
        Long userPersonId = person.getId();
        return !taskPersonId.equals(userPersonId);
    }
}
