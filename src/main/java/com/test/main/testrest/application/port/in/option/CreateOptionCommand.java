package com.test.main.testrest.application.port.in.option;

import com.test.main.testrest.domain.Option;
import lombok.Builder;
import lombok.Data;

public interface CreateOptionCommand {

    Option execute(OptionCommand command);

    @Data
    @Builder
    class OptionCommand {
        private Long quizId;
        private Long questionId;
        private String description;

        public Option toDomain() {
            return Option.builder()
                    .quizId(quizId)
                    .questionId(questionId)
                    .description(description)
                    .build();
        }
    }

}
