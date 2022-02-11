package com.test.main.testrest.application.usecase.question;

import com.test.main.testrest.application.port.in.question.DeleteQuestionCommand;
import com.test.main.testrest.application.port.out.QuestionJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeleteQuestionUseCase implements DeleteQuestionCommand {

    private final QuestionJpaRepository questionJpaRepository;

    public DeleteQuestionUseCase(QuestionJpaRepository questionJpaRepository) {
        this.questionJpaRepository = questionJpaRepository;
    }

    @Override
    public void execute(Long quizId, Long number) {
        log.info("Start - DeleteQuestionUseCase, quizId= {}, number= {}", quizId, number);
        this.questionJpaRepository.delete(quizId, number);
        log.info("End - DeleteQuestionUseCase, quizId= {}, number= {}", quizId, number);
    }

}
