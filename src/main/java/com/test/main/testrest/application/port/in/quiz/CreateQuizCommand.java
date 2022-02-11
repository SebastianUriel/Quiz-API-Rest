package com.test.main.testrest.application.port.in.quiz;

import com.test.main.testrest.domain.Quiz;
import lombok.Builder;
import lombok.Data;

public interface CreateQuizCommand {

    Quiz execute(QuizCommand command);

    @Data
    @Builder
    class QuizCommand {
        private String title;
        private String description;


        public Quiz toDomain() {
            return Quiz.builder()
                    .title(title)
                    .description(description)
                    .build();
        }
    }

}
