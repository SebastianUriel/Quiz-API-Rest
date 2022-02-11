package com.test.main.testrest.application.usecase.question;

import com.test.main.testrest.application.port.in.question.GetQuestionCommand;
import com.test.main.testrest.application.port.out.QuestionJpaRepository;
import com.test.main.testrest.domain.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class GetQuestionUseCase implements GetQuestionCommand {

    private final QuestionJpaRepository questionJpaRepository;

    public GetQuestionUseCase(QuestionJpaRepository questionJpaRepository) {
        this.questionJpaRepository = questionJpaRepository;
    }

    @Override
    public List<Question> execute(Long quizId) {
        log.info("Start - GetQuestionUseCase, quizId= {}", quizId);
        List<Question> questions = this.questionJpaRepository.get(quizId);
        log.info("End - GetQuestionUseCase, result: {}", questions);
        return questions;
    }

}
