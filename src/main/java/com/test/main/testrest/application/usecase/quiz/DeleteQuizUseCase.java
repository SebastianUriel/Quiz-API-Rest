package com.test.main.testrest.application.usecase.quiz;

import com.test.main.testrest.application.port.in.quiz.DeleteQuizCommand;
import com.test.main.testrest.application.port.out.QuizJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeleteQuizUseCase implements DeleteQuizCommand {

    public final QuizJpaRepository quizJpaRepository;

    public DeleteQuizUseCase(QuizJpaRepository quizJpaRepository) {
        this.quizJpaRepository = quizJpaRepository;
    }

    @Override
    public void execute(Long id) {
        log.info("Start - DeleteQuizUseCase, id: {}", id);
        this.quizJpaRepository.delete(id);
        log.info("End - DeleteQuizUseCase, id: {}", id);
    }

}
