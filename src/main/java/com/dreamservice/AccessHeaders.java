/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;

/**
 * @Author Nikita Salomatin
 * @Date 19.12.2017
 * @contact nsalomatin@hotmail.com
 */
public class AccessHeaders {

    public MultiValueMap<String, String> getHeaders() {
        org.springframework.util.MultiValueMap<String, String> headers = new HttpHeaders();
        headers.put("Access-Control-Allow-Origin", Arrays.asList("*"));
        headers.put("Access-Control-Allow-Credentials", Arrays.asList("true"));
        return headers;
    }
}
