package com.test.main.testrest.application.usecase.question;

import com.test.main.testrest.application.port.in.question.UpdateQuestionCommand;
import com.test.main.testrest.application.port.out.QuestionJpaRepository;
import com.test.main.testrest.domain.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpdateQuestionUseCase implements UpdateQuestionCommand {

    private final QuestionJpaRepository questionJpaRepository;

    public UpdateQuestionUseCase(QuestionJpaRepository questionJpaRepository) {
        this.questionJpaRepository = questionJpaRepository;
    }

    @Override
    public Question execute(QuestionCommand command) {
        log.info("Start - UpdateQuestionUseCase, command: {}", command);
        Question question = command.toDomain();
        question = this.questionJpaRepository.update(question);
        log.info("End - UpdateQuestionUseCase, result: {}", question);
        return question;
    }

}
