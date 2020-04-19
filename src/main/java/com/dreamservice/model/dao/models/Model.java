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
import java.util.Map;

/**
 * Created by Cok on 30.07.2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "ResultObject"
})
@EqualsAndHashCode
public class Model {

    @JsonProperty("ResultObject")
    private ResultObject resultObject;
    @JsonIgnore
    private Map<Category, Object> additionalProperties = new HashMap<Category, Object>();

    @JsonProperty("ResultObject")
    public ResultObject getResultObject() {
        return resultObject;
    }

    @JsonProperty("ResultObject")
    public void setResultObject(ResultObject resultObject) {
        this.resultObject = resultObject;
    }

    public Model withResultObject(ResultObject resultObject) {
        this.resultObject = resultObject;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<Category, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(Category name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Model withAdditionalProperty(Category name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(resultObject).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Model) == false) {
            return false;
        }
        Model rhs = ((Model) other);
        return new EqualsBuilder().append(resultObject, rhs.resultObject).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
