package com.test.main.testrest.application.port.in.question;

import com.test.main.testrest.domain.Question;

import java.util.List;

public interface GetQuestionCommand {

    List<Question> execute(Long quizId);

}
