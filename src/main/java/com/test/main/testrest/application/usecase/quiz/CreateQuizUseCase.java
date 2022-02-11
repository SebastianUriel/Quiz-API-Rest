package com.test.main.testrest.application.usecase.quiz;

import com.test.main.testrest.application.port.in.quiz.CreateQuizCommand;
import com.test.main.testrest.application.port.out.QuizJpaRepository;
import com.test.main.testrest.domain.Quiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreateQuizUseCase implements CreateQuizCommand {

    public final QuizJpaRepository quizJpaRepository;

    public CreateQuizUseCase(QuizJpaRepository quizJpaRepository) {
        this.quizJpaRepository = quizJpaRepository;
    }

    @Override
    public Quiz execute(QuizCommand command) {
        log.info("Start - CreateQuizUseCase, command: {}", command);
        Quiz quiz = command.toDomain();
        quiz = quizJpaRepository.create(quiz);
        log.info("End - CreateQuizUseCase, result: {}", quiz);
        return quiz;
    }

}
