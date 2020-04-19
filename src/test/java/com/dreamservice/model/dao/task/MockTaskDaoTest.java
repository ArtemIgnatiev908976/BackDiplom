/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.model.dao.task;

import com.dreamservice.model.dao.models.Task;
import com.dreamservice.model.dao.Dao;
import com.dreamservice.model.dao.MockTaskDao;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Cok on 05.08.2017.
 */
public class MockTaskDaoTest {

    private Dao<Task> mockItemDao;

    @Before
    @SuppressWarnings(value = "unchecked")
    public void setUp() throws Exception {
        mockItemDao = new MockTaskDao();
    }

    @Test
    public void createNewTask() throws Exception {

        int beforeSize = mockItemDao.findAll().size();
        Long id = mockItemDao.create(new Task().setName("create task"));
        int afterSize = mockItemDao.findAll().size();


        assertEquals("New task was not created", beforeSize + 1, afterSize);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findByExistId() throws Exception {

        String message = "find by exist id";
        Long id = mockItemDao.create(new Task().setName(message));
        Optional<Task> taskOptional = mockItemDao.findById(id);

        assertEquals("New task was not found", true, taskOptional.isPresent());
        assertEquals("New task name did not match to expected name", message, taskOptional.get().getName());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findByNotExistId() throws Exception {

        Long id = mockItemDao.create(new Task().setName("find by not exist id"));
        Optional<Task> taskOptional = mockItemDao.findById(id + 1);

        assertEquals("New task was not found", false, taskOptional.isPresent());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void updateTaskWhenFound() throws Exception {

        Task task = new Task().setName("find by not exist id");
        Long id = mockItemDao.create(task);
        boolean isFound = mockItemDao.update(new Task().setId(id).setName("updated"));
        Optional<Task> foundTask = mockItemDao.findById(id);
        if (!foundTask.isPresent()) fail("Could not find task after update");

        assertEquals("Result of update was false", true, isFound);
        assertEquals("Result object was not updated", "updated", foundTask.get().getName());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void updateTaskWhenNotFound() throws Exception {

        Task task = new Task().setName("find by not exist id");
        Long id = mockItemDao.create(task);
        Long id2 = mockItemDao.create(new Task().setName("task 2"));

        boolean isFound = mockItemDao.update(new Task().setId(1L).setName("updated"));

        assertEquals("Not exist task was successfully updated, it is wrong", false, isFound);
    }

}
