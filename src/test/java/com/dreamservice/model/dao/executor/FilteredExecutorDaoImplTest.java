/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.model.dao.executor;

import com.dreamservice.model.dao.models.Category;
import com.dreamservice.model.dao.models.City;
import com.dreamservice.model.dao.models.Executor;
import com.dreamservice.model.dao.models.Person;
import com.dreamservice.model.dao.Dao;
import com.dreamservice.model.dao.FilteredExecutorDaoImpl;
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
public class FilteredExecutorDaoImplTest {

    private List<Executor> expectedExecutors;
    private Dao<Executor> dao;

    @Before
    @SuppressWarnings(value = "unchecked")
    public void setUp() throws Exception {
        dao = mock(Dao.class);

        expectedExecutors = Arrays.asList(
                new Executor().setId(1L).setCategory(
                        new Category().setName("Web")
                ).setDescription("first executor")
                .setIsNewcomer(false)
                .setIsOnline(true)
                .setMinPriceForTask(343.55),
                new Executor().setId(2L).setCategory(
                        new Category().setName("Development")
                ).setDescription("second executor")
                        .setIsNewcomer(true)
                        .setIsOnline(false)
                        .setMinPriceForTask(345.55)
        );
        
        when(dao.findAll()).thenReturn(expectedExecutors);
    }

    @Test
    public void withoutFiltersShouldReturnAllExecutors() throws Exception {

        FilteredExecutorDaoImpl filteredExecutorDao = new FilteredExecutorDaoImpl(dao);

        List<Executor> actualExecutors = filteredExecutorDao.findAll();

        assertEquals("Executors without filters are not matched", expectedExecutors, actualExecutors);
    }

    @Test
    public void onlineFilterShouldReturnOnlineExecutors() throws Exception {

        FilteredExecutorDaoImpl filteredExecutorDao = new FilteredExecutorDaoImpl(dao);

        List<Executor> actualExecutors = filteredExecutorDao.online(true).findAll();

        assertEquals("Executors with online filters are not matched", expectedExecutors.get(0), actualExecutors.get(0));
        assertEquals("Executors with online filters are not matched", 1, actualExecutors.size());
    }

    @Test
    public void onlineFilterShouldReturnFilteredListWhenThereIsNoOnlineStatus() throws Exception {

        when(dao.findAll()).thenReturn(Arrays.asList(getTestExecutor(), getTestExecutor(), getTestExecutor()));
        FilteredExecutorDaoImpl filteredExecutorDao = new FilteredExecutorDaoImpl(dao);

        List<Executor> actualExecutors = filteredExecutorDao.online(true).findAll();
        assertEquals("Executors with online filters are not matched", 0, actualExecutors.size());
    }

    @Test
    public void falseOnlineFilterShouldReturnAllExecutors() throws Exception {

        FilteredExecutorDaoImpl filteredExecutorDao = new FilteredExecutorDaoImpl(dao);

        List<Executor> actualExecutors = filteredExecutorDao.online(false).findAll();

        assertEquals("Executors with online filters are not matched", 2, actualExecutors.size());
    }

    @Test
    public void newComersFilterShouldReturnNewcomersExecutors() throws Exception {

        FilteredExecutorDaoImpl filteredExecutorDao = new FilteredExecutorDaoImpl(dao);

        List<Executor> actualExecutors = filteredExecutorDao.newcomers(true).findAll();

        assertEquals("Executors with newcomers filters are not matched", expectedExecutors.get(1), actualExecutors.get(0));
        assertEquals("Executors with newcomers filters are not matched", 1, actualExecutors.size());
    }

    @Test
    public void falseNewComersFilterShouldReturnAllExecutors() throws Exception {

        FilteredExecutorDaoImpl filteredExecutorDao = new FilteredExecutorDaoImpl(dao);

        List<Executor> actualExecutors = filteredExecutorDao.newcomers(false).findAll();

        assertEquals("Executors with newcomers filters are not matched", 2, actualExecutors.size());
    }

    @Test
    public void withCategoryShouldReturnFilteredExecutorsByCategory() throws Exception {

        FilteredExecutorDaoImpl filteredExecutorDao = new FilteredExecutorDaoImpl(dao);

        List<Executor> actualExecutors = filteredExecutorDao.withCategory("Web").findAll();

        assertEquals("Executors with category filters are not matched", expectedExecutors.get(0), actualExecutors.get(0));
        assertEquals("Executors with category filters are not matched", 1, actualExecutors.size());
    }

    @Test
    public void withCategoryShouldBeNotCaseSensitive() throws Exception {

        FilteredExecutorDaoImpl filteredExecutorDao = new FilteredExecutorDaoImpl(dao);

        List<Executor> actualExecutors = filteredExecutorDao.withCategory("wEb").findAll();

        assertEquals("Executors with category filters are not matched", expectedExecutors.get(0), actualExecutors.get(0));
        assertEquals("Executors with category filters are not matched", 1, actualExecutors.size());
    }

    @Test
    public void withCategoryShouldWorkWhenExecutorDoesNotHaveCategory() throws Exception {

        FilteredExecutorDaoImpl filteredExecutorDao = new FilteredExecutorDaoImpl(dao);
        List<Executor> tasks = Collections.singletonList(new Executor().setId(1L));
        when(dao.findAll()).thenReturn(tasks);

        List<Executor> actualExecutors = filteredExecutorDao.withCategory("web").findAll();

        assertEquals("Executors with status filters are not matched", 0, actualExecutors.size());
    }

    private Executor getTestExecutor() {
        return new Executor().setMinPriceForTask(500.32)
                .setCategory(new Category().setName("Work"))
                .setDescription("desc1")
                .setPerson(new Person()
                        .setFirstName("Nik")
                        .setSecondName("Sal")
                        .setThirdName("Alex")
                        .setAge(23)
                        .setCity(new City().setName("SAMARA"))
                        .setSex("MALE"));
    }
}
