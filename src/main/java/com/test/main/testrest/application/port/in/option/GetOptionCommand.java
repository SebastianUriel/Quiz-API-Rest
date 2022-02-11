package com.test.main.testrest.application.port.in.option;

import com.test.main.testrest.domain.Option;

import java.util.List;

public interface GetOptionCommand {

    List<Option> execute(Long quizId, Long questionId);

}
