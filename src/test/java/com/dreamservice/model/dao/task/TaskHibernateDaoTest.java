/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.model.dao.task;

import com.dreamservice.model.dao.DBUtilService;
import com.dreamservice.model.dao.Dao;
import com.dreamservice.model.dao.HibernateDao;
import com.dreamservice.model.dao.models.*;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by Cok on 05.08.2017.
 */
public class TaskHibernateDaoTest {

    private Dao<Task> taskHibernateDao;
    private Dao<Category> categoryHibernateDao;
    private Dao<Customer> customerDao;
    private Dao<Person> personDao;
    private Dao<City> cityDao;

    @Before
    @SuppressWarnings(value = "unchecked")
    public void setUp() throws Exception {
        EntityManager manager = Persistence
                .createEntityManagerFactory(new DBUtilService().getPersistenceName())
                .createEntityManager();

        taskHibernateDao = new HibernateDao(manager, Task.class);
        categoryHibernateDao = new HibernateDao(manager, Category.class);
        customerDao = new HibernateDao(manager, Customer.class);
        personDao = new HibernateDao(manager, Person.class);
        cityDao = new HibernateDao(manager, City.class);

    }

    @Test
    public void createNewTask() throws Exception {

        Long samara = cityDao.create(new City().setName("SAMARA"));

        Long personId = personDao.create(new Person()
                .setAge(23)
                .setSex("MALE")
                .setCity(cityDao.findById(samara).get())
                .setSecondName("Sal")
                .setFirstName("Nikita")
                .setThirdName("Alex"));
        Person person = personDao.findById(personId).get();

        Long categoryId = categoryHibernateDao.create(new Category().setName("Test category"));
        Optional<Category> categoryOptional = categoryHibernateDao.findById(categoryId);

        Long customerId = customerDao.create(new Customer().setPerson(person));
        Optional<Customer> customerOptional = customerDao.findById(customerId);


        System.out.println(categoryId);
        if (!categoryOptional.isPresent()) {
            fail("Category was not saved");
        }
        int beforeSize = taskHibernateDao.findAll().size();
        Long id = taskHibernateDao.create(
                new Task().setPriceAmount(230.23)
                        .setPerson(person)
                        .setCategory(categoryOptional.get())
                        .setName("create task"));
        int afterSize = taskHibernateDao.findAll().size();


        assertEquals("New task was not created", beforeSize + 1, afterSize);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findByExistId() throws Exception {

        String message = "find by exist id";
        Long id = taskHibernateDao.create(new Task().setName(message));
        Optional<Task> taskOptional = taskHibernateDao.findById(id);

        assertEquals("New task was not found", true, taskOptional.isPresent());
        assertEquals("New task name did not match to expected name", message, taskOptional.get().getName());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findByNotExistId() throws Exception {

        Long id = taskHibernateDao.create(new Task().setName("find by not exist id"));
        Optional<Task> taskOptional = taskHibernateDao.findById(id + 1);

        assertEquals("New task was not found", false, taskOptional.isPresent());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void updateTaskWhenFound() throws Exception {

        Task task = new Task().setName("find by not exist id");
        Long id = taskHibernateDao.create(task);
        boolean isFound = taskHibernateDao.update(new Task().setId(id).setName("updated"));
        Optional<Task> foundTask = taskHibernateDao.findById(id);
        if (!foundTask.isPresent()) fail("Could not find task after update");

        assertEquals("Result of update was false, last exception was: " + taskHibernateDao.getException().toString(), true, isFound);
        assertEquals("Result object was not updated", "updated", foundTask.get().getName());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void updateTaskWhenNotFound() throws Exception {

        Task task = new Task().setName("find by not exist id");
        Long id = taskHibernateDao.create(task);
        Long id2 = taskHibernateDao.create(new Task().setName("task 2"));

        boolean isFound = taskHibernateDao.update(new Task().setId(id2+1).setName("updated"));

        assertEquals("Not exist task was successfully updated, it is wrong", false, isFound);
    }

    @Test
    public void deleteWhenFound() throws Exception {

        taskHibernateDao.create(new Task().setName("create task"));
        List<Task> all = taskHibernateDao.findAll();
        int beforeDeleting = all.size();
        taskHibernateDao.delete(all.get(0));
        int afterDeleting = taskHibernateDao.findAll().size();

        assertEquals("Task was not deleted", beforeDeleting-1, afterDeleting);
    }

    @Test
    public void deleteWhenNotFound() throws Exception {

        taskHibernateDao.create(new Task().setName("create task"));
        List<Task> all = taskHibernateDao.findAll();
        int beforeDeleting = all.size();
        Task task = all.get(0);
        taskHibernateDao.delete(new Task().setId(task.getId()+1).setName("not exist task"));
        int afterDeleting = taskHibernateDao.findAll().size();

        assertEquals("Not exist's task was deleted, it is wrong", beforeDeleting, afterDeleting);
    }

}
