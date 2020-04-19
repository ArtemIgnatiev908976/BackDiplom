/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.model.dao.task;

import com.dreamservice.model.dao.models.Category;
import com.dreamservice.model.dao.DBUtilService;
import com.dreamservice.model.dao.Dao;
import com.dreamservice.model.dao.HibernateDao;
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
public class CategoryHibernateDaoTest {

    private Dao<Category> categoryDao;

    @Before
    @SuppressWarnings(value = "unchecked")
    public void setUp() throws Exception {
        EntityManager manager = Persistence
                .createEntityManagerFactory(new DBUtilService().getPersistenceName())
                .createEntityManager();

        categoryDao = new HibernateDao(manager, Category.class);
    }

    @Test
    public void createNewCategory() throws Exception {

        int beforeSize = categoryDao.findAll().size();
        Long id = categoryDao.create(new Category().setName("create category"));
        int afterSize = categoryDao.findAll().size();


        assertEquals("New category was not created", beforeSize + 1, afterSize);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findByExistId() throws Exception {

        String message = "find by exist id";
        Long id = categoryDao.create(new Category().setName(message));
        Optional<Category> categoryOptional = categoryDao.findById(id);

        assertEquals("New category was not found", true, categoryOptional.isPresent());
        assertEquals("New category name did not match to expected name", message, categoryOptional.get().getName());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findByNotExistId() throws Exception {

        Long id = categoryDao.create(new Category().setName("find by not exist id"));
        Optional<Category> categoryOptional = categoryDao.findById(id + 1);

        assertEquals("New category was not found", false, categoryOptional.isPresent());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void updateCategoryWhenFound() throws Exception {

        Category category = new Category().setName("find by not exist id");
        Long id = categoryDao.create(category);
        boolean isFound = categoryDao.update(new Category().setId(id).setName("updated"));
        Optional<Category> foundCategory = categoryDao.findById(id);
        if (!foundCategory.isPresent()) fail("Could not find category after update");

        assertEquals("Result of update was false, last exception was: " + categoryDao.getException().toString(), true, isFound);
        assertEquals("Result object was not updated", "updated", foundCategory.get().getName());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void updateCategoryWhenNotFound() throws Exception {

        Category category = new Category().setName("find by not exist id");
        Long id = categoryDao.create(category);
        Long id2 = categoryDao.create(new Category().setName("category 2"));

        boolean isFound = categoryDao.update(new Category().setId(id2+1).setName("updated"));

        assertEquals("Not exist category was successfully updated, it is wrong", false, isFound);
    }

    @Test
    public void deleteWhenFound() throws Exception {

        categoryDao.create(new Category().setName("create category"));
        List<Category> all = categoryDao.findAll();
        int beforeDeleting = all.size();
        categoryDao.delete(all.get(0));
        int afterDeleting = categoryDao.findAll().size();

        assertEquals("Category was not deleted", beforeDeleting-1, afterDeleting);
    }

    @Test
    public void deleteWhenNotFound() throws Exception {

        categoryDao.create(new Category().setName("create category"));
        List<Category> all = categoryDao.findAll();
        int beforeDeleting = all.size();
        Category category = all.get(0);
        categoryDao.delete(new Category().setId(category.getId()+1).setName("not exist category"));
        int afterDeleting = categoryDao.findAll().size();

        assertEquals("Not exist's category was deleted, it is wrong", beforeDeleting, afterDeleting);
    }

}
