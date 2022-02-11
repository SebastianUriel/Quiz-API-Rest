package com.test.main.testrest.application.usecase.question;

import com.test.main.testrest.application.port.in.question.CreateQuestionCommand;
import com.test.main.testrest.application.port.out.QuestionJpaRepository;
import com.test.main.testrest.domain.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class CreateQuestionUseCase implements CreateQuestionCommand {

    private final QuestionJpaRepository questionJpaRepository;

    public CreateQuestionUseCase(QuestionJpaRepository questionJpaRepository) {
        this.questionJpaRepository = questionJpaRepository;
    }

    @Override
    public Question execute(QuestionCommand command) {
        log.info("Start - CreateQuestionUseCase, command: {}", command);
        Question question = command.toDomain();
        BigDecimal number = this.questionJpaRepository.nextNumber(command.getQuizId());
        question = question.withNumber(number.longValue());
        question = this.questionJpaRepository.create(question);
        log.info("End - CreateQuestionUseCase, result: {}", question);
        return question;
    }

}
