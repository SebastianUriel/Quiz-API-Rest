package com.test.main.testrest.application.port.out;

import com.test.main.testrest.domain.Option;

import java.math.BigDecimal;
import java.util.List;

public interface OptionJpaRepository {

    List<Option> get(Long quizId, Long questionId);
    Option create(Option option);
    Option update(Option option);
    void delete(Long quizId, Long questionId, Long number);
    BigDecimal nextNumber(Long quizId, Long questionId);

}
