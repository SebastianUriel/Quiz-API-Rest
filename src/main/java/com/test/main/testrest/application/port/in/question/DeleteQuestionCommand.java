package com.test.main.testrest.application.port.in.question;

public interface DeleteQuestionCommand {

    void execute(Long quizId, Long number);

}
