/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.model.dao.task;

import com.dreamservice.model.dao.models.Category;
import com.dreamservice.model.dao.models.Task;
import com.dreamservice.model.dao.Dao;
import com.dreamservice.model.dao.FilteredTaskDao;
import com.dreamservice.model.dao.FilteredTaskDaoImpl;
import com.dreamservice.model.dao.Status;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Cok on 05.08.2017.
 */
public class FilteredTaskDaoImplTest {

    private List<Task> expectedTasks;
    private Dao<Task> dao;

    @Before
    @SuppressWarnings(value = "unchecked")
    public void setUp() throws Exception {
        dao = mock(Dao.class);

        expectedTasks = Arrays.asList(
                new Task().setStatusFlag(Status.OPENED).setCategory(new Category().setName("Web")),
                new Task().setStatusFlag(Status.IN_PROGRESS).setCategory(new Category().setName("Some")));
        when(dao.findAll()).thenReturn(expectedTasks);
    }

    @Test
    public void withoutFiltersShouldReturnAllTasks() throws Exception {

        FilteredTaskDaoImpl filteredTaskDao = new FilteredTaskDaoImpl(dao);

        List<Task> actualTasks = filteredTaskDao.findAll();

        assertEquals("Tasks without filters are not matched", expectedTasks, actualTasks);
    }

    @Test
    public void withStatusShouldReturnFilteredTasksByStatus() throws Exception {

        FilteredTaskDaoImpl filteredTaskDao = new FilteredTaskDaoImpl(dao);

        List<Task> actualTasks = filteredTaskDao.withStatus(Status.IN_PROGRESS).findAll();

        assertEquals("Tasks with status filters are not matched", expectedTasks.get(1), actualTasks.get(0));
        assertEquals("Tasks with status filters are not matched", 1, actualTasks.size());
    }

    @Test
    public void withAnyStatusShouldReturnAllTask() throws Exception {

        FilteredTaskDao filteredTaskDao = new FilteredTaskDaoImpl(dao);

        List<Task> actualTasks = filteredTaskDao.withStatus(Status.ANY).findAll();

        assertEquals("Tasks with any status filters are not matched", 2, actualTasks.size());
    }

    @Test
    public void withCategoryShouldReturnFilteredTasksByCategory() throws Exception {

        FilteredTaskDaoImpl filteredTaskDao = new FilteredTaskDaoImpl(dao);

        List<Task> actualTasks = filteredTaskDao.withCategory("Web").findAll();

        assertEquals("Tasks with category filters are not matched", expectedTasks.get(0), actualTasks.get(0));
        assertEquals("Tasks with category filters are not matched", 1, actualTasks.size());
    }

    @Test
    public void withCategoryShouldBeNotCaseSensitive() throws Exception {

        FilteredTaskDaoImpl filteredTaskDao = new FilteredTaskDaoImpl(dao);

        List<Task> actualTasks = filteredTaskDao.withCategory("wEb").findAll();

        assertEquals("Tasks with category filters are not matched", expectedTasks.get(0), actualTasks.get(0));
        assertEquals("Tasks with category filters are not matched", 1, actualTasks.size());
    }

    @Test
    public void withStatusShouldWorkWhenTaskDoesNotHaveStatus() throws Exception {

        FilteredTaskDaoImpl filteredTaskDao = new FilteredTaskDaoImpl(dao);
        List<Task> tasks = Collections.singletonList(new Task().setName("task without status"));
        when(dao.findAll()).thenReturn(tasks);

        List<Task> actualTasks = filteredTaskDao.withStatus(Status.OPENED).findAll();

        assertEquals("Tasks with status filters are not matched", 0, actualTasks.size());
    }

    @Test
    public void withCategoryShouldWorkWhenTaskDoesNotHaveCategory() throws Exception {

        FilteredTaskDaoImpl filteredTaskDao = new FilteredTaskDaoImpl(dao);
        List<Task> tasks = Collections.singletonList(new Task().setName("task without status"));
        when(dao.findAll()).thenReturn(tasks);

        List<Task> actualTasks = filteredTaskDao.withCategory("web").findAll();

        assertEquals("Tasks with status filters are not matched", 0, actualTasks.size());
    }
}
