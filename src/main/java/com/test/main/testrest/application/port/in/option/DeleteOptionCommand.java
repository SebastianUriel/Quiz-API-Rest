package com.test.main.testrest.application.port.in.option;

public interface DeleteOptionCommand {

    void execute(Long quizId, Long questionId, Long number);

}
