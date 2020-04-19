package com.dreamservice.model.dao;

/**
 * Status of task
 */
public enum Status {

    OPENED("OPENED"),
    IN_PROGRESS("IN_PROGRESS"),
    ACCEPTED("ACCEPTED"),
    CLOSED("CLOSED"),
    ANY("ANY");


    Status(String inputName) {
        this.name = inputName;
    };

    private String name;


    public String getName() {
       return name;
   }

    @Override
    public String toString() {
        return name;
    }
}
