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

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "person",
        "rating"
})
@Entity
@Table(name = "Customer")
@NoArgsConstructor
@NamedQuery(name = "Customer.findAll", query = "from Customer")
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
public class Customer implements HasId, SetId<Customer> {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("rating")
    private Rating rating;

    @JsonProperty("person")
    private Person person;

    @JsonIgnore
    @Transient
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("Rating")
    @Access(AccessType.PROPERTY)
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Rating.class)
    @JoinColumn(name = "RATING", nullable = true, foreignKey = @ForeignKey(name = "CUSTOMER_FK_RATING"))
    public Rating getRating() {
        return rating;
    }

    @JsonProperty("person")
    @Access(AccessType.PROPERTY)
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Person.class)
    @JoinColumn(name = "PERSON", nullable = true, foreignKey = @ForeignKey(name = "CUSTOMER_FK_PERSON"))
    public Person getPerson() {
        return person;
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

}