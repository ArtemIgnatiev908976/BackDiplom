
/*
 * Copyright (c) 2017.
 * Unauthorized copying as well as modification and distribution of this file is strictly prohibited
 * Proprietary and confidential
 * Written by Nikita Salomatin <nsalomatin@hotmail.com>
 */

package com.dreamservice.model.dao.models;

import com.dreamservice.model.dao.HasId;
import com.dreamservice.model.dao.SetId;
import com.dreamservice.model.dao.Status;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Cok on 30.07.2017.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "stringId",
        "name",
        "description",
        "priceAmount",
        "statusFlag",
        "category",
        "isDraft",
        "person",
        "executorId",
        "isRegular",
        "respondExecutors"
})
@Entity
@Table(name = "Task")
@NoArgsConstructor
@NamedQuery(name = "Task.findAll", query = "from Task")
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
public class Task implements HasId, SetId<Task> {

    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private Long id;

    @NonNull
    @JsonProperty("stringId")
    private String stringId;

    @JsonProperty("startDate")
    @Column
    private String startDate;

    @JsonProperty("endDate")
    @Column
    private String endDate;

    @Column
    @JsonProperty("name")
    private String name;

    @Column
    @JsonProperty("description")
    private String description;

    @Column
    @JsonProperty("priceAmount")
    private double priceAmount;

    @Column
    @JsonProperty("statusFlag")
    @Enumerated(EnumType.ORDINAL)
    private Status statusFlag = Status.OPENED;

    @Column
    @JsonProperty("category")
    @Type(type = "bigint")
    private Category category;

    @Column
    @JsonProperty("isDraft")
    private Boolean isDraft = true;

    @Column
    @JsonProperty("person")
    private Person person;

    @Column
    @JsonProperty("executorId")
    private Long executorId;

    @Column
    @JsonProperty("isRegular")
    private Boolean isRegular = false;

    @Column
    @JsonProperty("address")
    private String address = "";

    @JsonProperty("respondExecutors")
    private Set<Executor> respondExecutors = new HashSet<>();

    @JsonIgnore
    @Transient
    private Map<java.lang.String, Object> additionalProperties = new HashMap<java.lang.String, Object>();

    @JsonProperty("category")
    @Access(AccessType.PROPERTY)
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Category.class)
    @JoinColumn(name = "CATEGORY", nullable = true, foreignKey = @ForeignKey(name = "TASK_FK_CATEGORY"))
    public Category getCategory() {
        return category;
    }

    @JsonProperty("person")
    @Access(AccessType.PROPERTY)
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Customer.class)
    @JoinColumn(name = "PERSON", nullable = true, foreignKey = @ForeignKey(name = "TASK_FK_PERSON"))
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

    public Task addRespond(Executor executor) {
         this.respondExecutors.add(executor);
         return this;
    }
}
