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
 * @Date 19.12.2017
 * @contact nsalomatin@hotmail.com
 */
public class LoginPasswordComparison implements Predicate<Auth> {

    private final String email;
    private final String password;

    public LoginPasswordComparison(String email, String password){
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean test(Auth auth) {
        return auth.getEmail().equals(email) && auth.getPassword().equals(password);

    }
}
