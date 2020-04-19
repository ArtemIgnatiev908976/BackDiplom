/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice;

import com.dreamservice.model.dao.models.Auth;

import java.util.function.Predicate;

/**
 * @Author Nikita Salomatin
 * @Date 23.12.2017
 * @contact nsalomatin@hotmail.com
 */
public class LoginComparison implements Predicate<Auth> {
    private String login;

    public LoginComparison(String login) {
        this.login = login;
    }

    @Override
    public boolean test(Auth auth) {
        return auth.getEmail().equals(login);
    }
}
