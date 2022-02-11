package com.test.main.testrest.adapter.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.test.main.testrest.domain.Quiz;
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
public class QuizResponse {

    private Long id;
    private String title;
    private String description;
    private List<QuestionResponse> questions;

    public static QuizResponse fromDomain(Quiz domain) {
        return Optional.ofNullable(domain)
                .map(quiz -> QuizResponse.builder()
                        .id(quiz.getId())
                        .title(quiz.getTitle())
                        .description(quiz.getDescription())
                        .questions(Optional.ofNullable(quiz.getQuestions())
                                .map(values -> values.stream()
                                        .map(QuestionResponse::fromDomain)
                                        .collect(Collectors.toList()))
                                .orElse(null))
                        .build())
                .orElse(null);
    }

}
