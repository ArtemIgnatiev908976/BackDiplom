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
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "firstName",
        "secondName",
        "thirdName",
        "age",
        "sex",
        "city"
})
@Entity
@Table(name = "Person")
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
public class Person implements HasId, SetId<Person> {

    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("firstName")
    @Column
    private String firstName = "";

    @JsonProperty("secondName")
    @Column
    private String secondName;

    @Column
    @JsonProperty("thirdName")
    private String thirdName;

    @Column
    @JsonProperty("age")
    private Integer age;

    @Column
    @JsonProperty("sex")
    private String sex;

    @Column
    @JsonProperty("city")
    private City city;

    @JsonProperty("phone")
    @Column
    private String phone = "";

    @JsonProperty("email")
    @Column
    private String email;

    @JsonIgnore
    @Transient
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("city")
    @Access(AccessType.PROPERTY)
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = City.class)
    @JoinColumn(name = "City", nullable = true, foreignKey = @ForeignKey(name = "PERSON_FK_CITY"))
    public City getCity() {
        return city;
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
