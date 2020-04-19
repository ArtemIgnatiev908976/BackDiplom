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
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "PositiveReviews",
        "NegativeReviews",
        "SummaryScore",
        "SummaryReviews"
})
@Entity
@Table(name = "Rating")
@NoArgsConstructor
@NamedQuery(name = "Rating.findAll", query = "from Rating")
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
public class Rating implements HasId, SetId<Rating> {

    @NonNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("id")
    private Long id;

    @JsonProperty("PositiveReviews")
    @Column
    private Integer positiveReviews;
    @Column
    @JsonProperty("NegativeReviews")
    private Integer negativeReviews;
    @Column
    @JsonProperty("SummaryScore")
    private Double summaryScore;
    @Column
    @JsonProperty("SummaryReviews")
    private Integer summaryReviews;

    @JsonIgnore
    @Transient
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
