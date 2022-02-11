package com.test.main.testrest.application.port.out;

import com.test.main.testrest.domain.Quiz;

import java.util.List;

public interface QuizJpaRepository {

    List<Quiz> getAll();
    Quiz get(Long id);
    Quiz create(Quiz quiz);
    Quiz update(Quiz quiz);
    void delete(Long id);

}
