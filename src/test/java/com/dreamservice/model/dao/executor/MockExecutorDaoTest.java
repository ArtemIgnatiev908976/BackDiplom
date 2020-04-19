/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.model.dao.executor;

import com.dreamservice.model.dao.models.Executor;
import com.dreamservice.model.dao.Dao;
import com.dreamservice.model.dao.MockExecutorDao;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Cok on 05.08.2017.
 */
public class MockExecutorDaoTest {

    private Dao<Executor> mockExecutorDao;

    @Before
    @SuppressWarnings(value = "unchecked")
    public void setUp() throws Exception {
        mockExecutorDao = new MockExecutorDao();
    }

    @Test
    public void createNewExecutor() throws Exception {

        int beforeSize = mockExecutorDao.findAll().size();
        Long id = mockExecutorDao.create(new Executor().setDescription("test description"));
        int afterSize = mockExecutorDao.findAll().size();


        assertEquals("New executor was not created", beforeSize + 1, afterSize);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findByExistId() throws Exception {

        String message = "test description";
        Long id = mockExecutorDao.create(new Executor().setDescription(message));
        Optional<Executor> executorOptional = mockExecutorDao.findById(id);

        assertEquals("New executor was not found", true, executorOptional.isPresent());
        assertEquals("New executor name did not match to expected name", message, executorOptional.get().getDescription());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findByNotExistId() throws Exception {

        Long id = mockExecutorDao.create(new Executor().setDescription("find by not exist id"));
        Optional<Executor> executorOptional = mockExecutorDao.findById(id + 1);

        assertEquals("New executor was not found", false, executorOptional.isPresent());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void updateExecutorWhenFound() throws Exception {

        Executor executor = new Executor().setDescription("find by not exist id");
        Long id = mockExecutorDao.create(executor);
        boolean isFound = mockExecutorDao.update(new Executor().setId(id).setDescription("updated"));
        Optional<Executor> foundExecutor = mockExecutorDao.findById(id);
        if (!foundExecutor.isPresent()) fail("Could not find executor after update");

        assertEquals("Result of update was false", true, isFound);
        assertEquals("Result object was not updated", "updated", foundExecutor.get().getDescription());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void updateExecutorWhenNotFound() throws Exception {

        Executor executor = new Executor().setDescription("find by not exist id");
        Long id = mockExecutorDao.create(executor);
        Long id2 = mockExecutorDao.create(new Executor().setDescription("executor 2"));

        boolean isFound = mockExecutorDao.update(new Executor().setId(1L).setDescription("updated"));

        assertEquals("Not exist executor was successfully updated, it is wrong", false, isFound);
    }

}
