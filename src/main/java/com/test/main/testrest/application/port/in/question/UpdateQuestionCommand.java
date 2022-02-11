package com.test.main.testrest.application.port.in.question;

import com.test.main.testrest.domain.Question;
import com.test.main.testrest.domain.TypeOption;
import lombok.Builder;
import lombok.Data;

public interface UpdateQuestionCommand {

    Question execute(QuestionCommand command);

    @Data
    @Builder
    class QuestionCommand {
        private Long quizId;
        private Long number;
        private String description;
        private TypeOption type;

        public Question toDomain() {
            return Question.builder()
                    .quizId(quizId)
                    .number(number)
                    .description(description)
                    .type(type)
                    .build();
        }
    }

}
