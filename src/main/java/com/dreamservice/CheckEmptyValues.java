/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice;

/**
 * @Author Nikita Salomatin
 * @Date 23.12.2017
 * @contact nsalomatin@hotmail.com
 */
public class CheckEmptyValues {

    private final String login;
    private final String password;

    public CheckEmptyValues(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public void assertEmpty() throws EmptyCredentialException {
        if ("".equals(login) || "".equals(password)) {
            throw new EmptyCredentialException();
        }
    }
}
