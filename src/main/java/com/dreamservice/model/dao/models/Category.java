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
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Task category
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "name"
})
@Entity()
@Table(name = "Category")
@NoArgsConstructor
@NamedQuery(name = "Category.findAll", query = "Select c from Category c")
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
public class Category implements HasId, SetId<Category> {

    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    @Getter
    private Long id;

    @Column
    @NonNull
    @JsonProperty("name")
    private String name;

    @JsonIgnore
    @Transient
    private Map<java.lang.String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
