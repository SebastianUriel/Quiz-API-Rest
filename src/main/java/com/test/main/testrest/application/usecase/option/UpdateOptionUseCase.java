package com.test.main.testrest.application.usecase.option;

import com.test.main.testrest.application.port.in.option.UpdateOptionCommand;
import com.test.main.testrest.application.port.out.OptionJpaRepository;
import com.test.main.testrest.domain.Option;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpdateOptionUseCase implements UpdateOptionCommand {

    private final OptionJpaRepository optionJpaRepository;

    public UpdateOptionUseCase(OptionJpaRepository optionJpaRepository) {
        this.optionJpaRepository = optionJpaRepository;
    }

    @Override
    public Option execute(OptionCommand command) {
        log.info("Start - UpdateOptionUseCase, command: {}", command);
        Option option = command.toDomain();
        option = this.optionJpaRepository.update(option);
        log.info("End - UpdateOptionUseCase, result: {}", option);
        return option;
    }

}
