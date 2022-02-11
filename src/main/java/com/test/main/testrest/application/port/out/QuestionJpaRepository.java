package com.test.main.testrest.application.port.out;

import com.test.main.testrest.domain.Question;

import java.math.BigDecimal;
import java.util.List;

public interface QuestionJpaRepository {

    List<Question> get(Long quizId);
    Question create(Question question);
    Question update(Question question);
    void delete(Long quizId, Long number);
    BigDecimal nextNumber(Long quizId);

}
