package com.test.main.testrest.adapter.jpa.entity;

import com.test.main.testrest.adapter.jpa.entity.id.QuestionId;
import com.test.main.testrest.domain.Question;
import com.test.main.testrest.domain.TypeOption;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity()
@Table(name = "question")
public class QuestionEntity {

    @EmbeddedId
    private QuestionId questionId;

    @Column(name = "description", columnDefinition = "varchar(225) not null")
    private String description;

    @Column(name = "type", columnDefinition = "varchar(30) CHECK (type = 'OPEN' OR type = 'MULTIPLE_OPTION' OR type = 'ONE_OPTION')")
    private String type;

    @Column(name = "available", columnDefinition = "boolean not null default 1")
    private Boolean available;

    public Question toDomain() {
        return Question.builder()
                .quizId(questionId.getQuizId())
                .number(questionId.getNumber())
                .description(description)
                .type(TypeOption.valueOf(type))
                .build();
    }

    public static QuestionEntity fromDomain(Question question) {
        return QuestionEntity.builder()
                .questionId(QuestionId.builder()
                        .quizId(question.getQuizId())
                        .number(question.getNumber())
                        .build())
                .description(question.getDescription())
                .type(question.getType().toString())
                .available(true)
                .build();
    }

}
