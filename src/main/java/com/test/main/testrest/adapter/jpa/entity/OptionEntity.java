package com.test.main.testrest.adapter.jpa.entity;

import com.test.main.testrest.adapter.jpa.entity.id.OptionId;
import com.test.main.testrest.domain.Option;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity()
@Table(name = "qn_option")
public class OptionEntity {

    @EmbeddedId
    private OptionId optionId;

    @Column(name = "description", columnDefinition = "varchar(225) not null")
    private String description;

    @Column(name = "available", columnDefinition = "boolean not null default 1")
    private Boolean available;

    public Option toDomain() {
        return Option.builder()
                .quizId(optionId.getQuizId())
                .questionId(optionId.getQuestionId())
                .number(optionId.getNumber())
                .description(description)
                .build();
    }

    public static OptionEntity fromDomain(Option option) {
        return OptionEntity.builder()
                .optionId(OptionId.builder()
                        .quizId(option.getQuizId())
                        .questionId(option.getQuestionId())
                        .number(option.getNumber())
                        .build())
                .description(option.getDescription())
                .available(true)
                .build();
    }

}
