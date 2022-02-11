package com.test.main.testrest.application.usecase.option;

import com.test.main.testrest.application.port.in.option.DeleteOptionCommand;
import com.test.main.testrest.application.port.out.OptionJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeleteOptionUseCase implements DeleteOptionCommand {

    private final OptionJpaRepository optionJpaRepository;

    public DeleteOptionUseCase(OptionJpaRepository optionJpaRepository) {
        this.optionJpaRepository = optionJpaRepository;
    }

    @Override
    public void execute(Long quizId, Long questionId, Long number) {
        log.info("Start - DeleteOptionUseCase, quizId= {}, questionId= {}, number= {}", quizId, questionId, number);
        this.optionJpaRepository.delete(quizId, questionId, number);
        log.info("End - DeleteOptionUseCase, quizId= {}, questionId= {}, number= {}", quizId, questionId, number);
    }

}
