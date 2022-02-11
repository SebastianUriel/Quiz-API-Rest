package com.test.main.testrest.adapter.controller.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.test.main.testrest.domain.Option;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OptionResponse {

    private Long quizId;
    private Long questionId;
    private Long number;
    private String description;
    private Boolean isSelected;

    public static OptionResponse fromDomain(Option domain) {
        return Optional.ofNullable(domain)
                .map(option -> OptionResponse.builder()
                        .quizId(option.getQuizId())
                        .questionId(option.getQuestionId())
                        .number(option.getNumber())
                        .description(option.getDescription())
                        .build())
                .orElse(null);
    }

}
