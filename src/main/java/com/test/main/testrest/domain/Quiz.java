package com.test.main.testrest.domain;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.util.List;

@Value
@Builder
public class Quiz {

    Long id;
    String title;
    String description;

    @With
    List<Question> questions;

}
