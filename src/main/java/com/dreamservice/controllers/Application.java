/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.controllers;

import com.dreamservice.DaoFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        DaoController.setInstance(DaoFactory.getDaoController("testing"));
        application.run(args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:63343",
                        "http://localhost:63342", "http://localhost:5000", "https://artemignatiev908976.github.io/diplomTest", "http://arcane-waters-26008.herokuapp.com");
                registry.addMapping("/task/").allowedOrigins("http://localhost:63343",
                        "http://localhost:63342", "http://localhost:5000", "https://artemignatiev908976.github.io/diplomTest", "http://arcane-waters-26008.herokuapp.com");
            }
        };
    }
}
