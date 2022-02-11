package com.test.main.testrest.application.usecase.option;

import com.test.main.testrest.application.port.in.option.GetOptionCommand;
import com.test.main.testrest.application.port.out.OptionJpaRepository;
import com.test.main.testrest.domain.Option;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class GetOptionUseCase implements GetOptionCommand {

    private final OptionJpaRepository optionJpaRepository;

    public GetOptionUseCase(OptionJpaRepository optionJpaRepository) {
        this.optionJpaRepository = optionJpaRepository;
    }

    @Override
    public List<Option> execute(Long quizId, Long questionId) {
        log.info("Start - GetOptionUseCase, quizId= {}, questionId= {}", quizId, questionId);
        List<Option> options = this.optionJpaRepository.get(quizId, questionId);
        log.info("End - GetOptionUseCase, result: {}", options);
        return options;
    }

}
