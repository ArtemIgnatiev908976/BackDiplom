/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.model.dao.models;

import com.fasterxml.jackson.annotation.*;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Cok on 30.07.2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Items",
        "Executors",
        "TotalAmount"
})
@EqualsAndHashCode
public class ResultObject {

    @JsonProperty("Items")
    private List<Task> tasks = null;

    @JsonProperty("Executors")
    private List<Executor> executors = null;

    @JsonProperty("TotalAmount")
    private int totalAmount = 0;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Items")
    public List<Task> getTasks() {
        return tasks;
    }

    @JsonProperty("TotalAmount")
    public Integer getTotalAmount() {
        return totalAmount;
    }

    @JsonProperty("TotalAmount")
    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    @JsonProperty("Items")
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    @JsonProperty("Executors")
    public List<Executor> getExecutors() {
        return executors;
    }

    @JsonProperty("Executors")
    public void setExecutors(List<Executor> executors) {
        this.executors = executors;
    }

    public ResultObject withItems(List<Task> tasks) {
        this.tasks = tasks;
        return this;
    }

    public ResultObject withExecutors(List<Executor> executors) {
        this.executors = executors;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public ResultObject withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(executors).append(tasks).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ResultObject) == false) {
            return false;
        }
        ResultObject rhs = ((ResultObject) other);
        return new EqualsBuilder().append(executors, rhs.executors).append(tasks, rhs.tasks).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    public ResultObject withTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }
}
