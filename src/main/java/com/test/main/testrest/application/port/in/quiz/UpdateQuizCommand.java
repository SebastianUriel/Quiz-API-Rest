package com.test.main.testrest.application.port.in.quiz;

import com.test.main.testrest.domain.Quiz;
import lombok.Builder;
import lombok.Data;

public interface UpdateQuizCommand {

    Quiz execute(UpdateQuizCommand.QuizCommand command);

    @Data
    @Builder
    class QuizCommand {
        private Long id;
        private String title;
        private String description;

        public Quiz toDomain() {
            return Quiz.builder()
                    .id(id)
                    .title(title)
                    .description(description)
                    .build();
        }
    }

}
