package com.test.main.testrest.domain;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.List;

@Value
@Builder
public class Question {

    Long quizId;

    @With
    Long number;

    String description;
    TypeOption type;

    @With
    List<Option> options;

}
