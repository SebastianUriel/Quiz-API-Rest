package com.test.main.testrest.application.usecase.option;

import com.test.main.testrest.application.port.in.option.CreateOptionCommand;
import com.test.main.testrest.application.port.out.OptionJpaRepository;
import com.test.main.testrest.domain.Option;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class CreateOptionUseCase implements CreateOptionCommand {

    private final OptionJpaRepository optionJpaRepository;

    public CreateOptionUseCase(OptionJpaRepository optionJpaRepository) {
        this.optionJpaRepository = optionJpaRepository;
    }

    @Override
    public Option execute(OptionCommand command) {
        log.info("Start - CreateOptionUseCase, command: {}", command);
        Option option = command.toDomain();
        BigDecimal number = this.optionJpaRepository.nextNumber(command.getQuizId(), command.getQuestionId());
        option = option.withNumber(number.longValue());
        option = this.optionJpaRepository.create(option);
        log.info("End - CreateOptionUseCase, result: {}", option);
        return option;
    }

}
