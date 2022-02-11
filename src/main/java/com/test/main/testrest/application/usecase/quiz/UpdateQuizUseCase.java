package com.test.main.testrest.application.usecase.quiz;

import com.test.main.testrest.application.port.in.quiz.UpdateQuizCommand;
import com.test.main.testrest.application.port.out.QuizJpaRepository;
import com.test.main.testrest.domain.Quiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpdateQuizUseCase implements UpdateQuizCommand {

    public final QuizJpaRepository quizJpaRepository;

    public UpdateQuizUseCase(QuizJpaRepository quizJpaRepository) {
        this.quizJpaRepository = quizJpaRepository;
    }

    @Override
    public Quiz execute(QuizCommand command) {
        log.info("Start - UpdateQuizUseCase, command: {}", command);
        Quiz quiz = command.toDomain();
        quiz = quizJpaRepository.update(quiz);
        log.info("End - UpdateQuizUseCase, result: {}", quiz);
        return quiz;
    }

}
