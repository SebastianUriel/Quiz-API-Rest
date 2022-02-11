package com.test.main.testrest.application.port.in.quiz;

import com.test.main.testrest.domain.Quiz;

public interface GetQuizCommand {

    Quiz execute(Long id);

}
