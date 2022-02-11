package com.test.main.testrest.usecase.option;

import com.test.main.testrest.application.port.in.option.CreateOptionCommand;
import com.test.main.testrest.application.port.out.OptionJpaRepository;
import com.test.main.testrest.application.usecase.option.CreateOptionUseCase;
import com.test.main.testrest.config.TestConfig;
import com.test.main.testrest.config.exception.ErrorCode;
import com.test.main.testrest.config.exception.GenericException;
import com.test.main.testrest.domain.Option;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@Import(TestConfig.class)
public class CreateOptionUseCaseTest {

    private final Long QUIZ_ID = 1l;
    private final Long QUESTION_ID = 1l;
    private final OptionJpaRepository optionJpaRepository = mock(OptionJpaRepository.class);
    private final CreateOptionUseCase useCase = new CreateOptionUseCase(optionJpaRepository);

    @Test
    @DisplayName("Cuando intento crear una opcion correctamente, entonces regreso la opcion creada")
    public void executeOk() {
        CreateOptionCommand.OptionCommand command = getCommandMock();
        Option expected = getOptionMock();
        BigDecimal nextNumber = getNextNumberMock();

        when(optionJpaRepository.nextNumber(eq(QUIZ_ID), eq(QUESTION_ID))).thenReturn(nextNumber);
        when(optionJpaRepository.create(eq(expected))).thenReturn(expected);

        Option result = useCase.execute(command);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);

        verify(optionJpaRepository, times(1)).nextNumber(eq(QUIZ_ID), eq(QUESTION_ID));
        verify(optionJpaRepository, times(1)).create(eq(expected));
    }

    @Test
    @DisplayName("Cuando hay un error al intentar crear una opcion, entonces lanzo una excepcion")
    public void executeError() {
        CreateOptionCommand.OptionCommand command = getCommandMock();
        Option expected = getOptionMock();
        BigDecimal nextNumber = getNextNumberMock();

        when(optionJpaRepository.nextNumber(eq(QUIZ_ID), eq(QUESTION_ID))).thenReturn(nextNumber);
        when(optionJpaRepository.create(eq(expected))).thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));

        Throwable throwable = catchThrowable(() -> useCase.execute(command));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(optionJpaRepository, times(1)).nextNumber(eq(QUIZ_ID), eq(QUESTION_ID));
        verify(optionJpaRepository, times(1)).create(eq(expected));
    }

    public CreateOptionCommand.OptionCommand getCommandMock() {
        return CreateOptionCommand.OptionCommand.builder()
                .quizId(1l)
                .questionId(1l)
                .description("DESCRIPTION")
                .build();
    }

    public Option getOptionMock() {
        return Option.builder()
                .quizId(1l)
                .questionId(1l)
                .number(1l)
                .description("DESCRIPTION")
                .build();
    }

    public BigDecimal getNextNumberMock() {
        return new BigDecimal(1l);
    }

}
