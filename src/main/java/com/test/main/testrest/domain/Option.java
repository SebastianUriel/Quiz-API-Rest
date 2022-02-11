package com.test.main.testrest.domain;

import lombok.Builder;
import lombok.Value;
import lombok.With;

@Value
@Builder
public class Option {

    Long quizId;
    Long questionId;

    @With
    Long number;

    String description;

}
