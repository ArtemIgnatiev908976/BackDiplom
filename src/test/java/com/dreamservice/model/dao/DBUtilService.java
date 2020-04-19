package com.dreamservice.model.dao;

/**
 * Created by Cok on 19.08.2017.
 */
public class DBUtilService {
    public String getPersistenceName() {
        return "test-" + System.getProperty("db", "h2");
    }
}
