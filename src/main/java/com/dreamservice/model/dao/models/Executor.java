/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.model.dao.models;

import com.dreamservice.model.dao.HasId;
import com.dreamservice.model.dao.SetId;
import com.fasterxml.jackson.annotation.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "stringId",
        "person",
        "minPriceForTask",
        "category",
        "description"
})
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
public class Executor implements HasId, SetId<Executor> {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("stringId")
    private String stringId;

    @JsonProperty("person")
    private Person person;

    @JsonProperty("minPriceForTask")
    private double minPriceForTask;

    @JsonProperty("category")
    private Category category;

    @JsonProperty("description")
    private String description;

    @JsonProperty("isOnline")
    private Boolean isOnline = false;

    @JsonProperty("isNewcomer")
    private Boolean isNewcomer = true;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

}
