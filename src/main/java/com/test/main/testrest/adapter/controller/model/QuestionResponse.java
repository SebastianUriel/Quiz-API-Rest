package com.test.main.testrest.adapter.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.test.main.testrest.domain.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionResponse {

    private Long quizId;
    private Long number;
    private String description;
    private String type;
    private List<OptionResponse> options;
    private String answer;

    public static QuestionResponse fromDomain(Question domain) {
        return Optional.ofNullable(domain)
                .map(question -> QuestionResponse.builder()
                        .quizId(question.getQuizId())
                        .number(question.getNumber())
                        .description(question.getDescription())
                        .type(question.getType().toString())
                        .options(Optional.ofNullable(question.getOptions())
                                .map(values -> values.stream()
                                        .map(OptionResponse::fromDomain)
                                        .collect(Collectors.toList()))
                                .orElse(null))
                        .build())
                .orElse(null);
    }

}
