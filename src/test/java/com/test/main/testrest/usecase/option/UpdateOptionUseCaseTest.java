package com.test.main.testrest.usecase.option;

import com.test.main.testrest.application.port.in.option.UpdateOptionCommand;
import com.test.main.testrest.application.port.out.OptionJpaRepository;
import com.test.main.testrest.application.usecase.option.UpdateOptionUseCase;
import com.test.main.testrest.config.TestConfig;
import com.test.main.testrest.config.exception.ErrorCode;
import com.test.main.testrest.config.exception.GenericException;
import com.test.main.testrest.domain.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@Import(TestConfig.class)
public class UpdateOptionUseCaseTest {

    private final OptionJpaRepository optionJpaRepository = mock(OptionJpaRepository.class);
    private final UpdateOptionUseCase useCase = new UpdateOptionUseCase(optionJpaRepository);

    @Test
    @DisplayName("Cuando intento actualizar una opcion correctamente, entonces regreso la opcion creada")
    public void executeOk() {
        UpdateOptionCommand.OptionCommand command = getCommandMock();
        Option expected = getOptionMock();

        when(optionJpaRepository.update(eq(expected))).thenReturn(expected);

        Option result = useCase.execute(command);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);

        verify(optionJpaRepository, times(1)).update(eq(expected));
    }

    @Test
    @DisplayName("Cuando hay un error al intentar actualizar una opcion, entonces lanzo una excepcion")
    public void executeError() {
        UpdateOptionCommand.OptionCommand command = getCommandMock();
        Option expected = getOptionMock();

        when(optionJpaRepository.update(eq(expected))).thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));

        Throwable throwable = catchThrowable(() -> useCase.execute(command));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(optionJpaRepository, times(1)).update(eq(expected));
    }

    public UpdateOptionCommand.OptionCommand getCommandMock() {
        return UpdateOptionCommand.OptionCommand.builder()
                .quizId(1l)
                .questionId(1l)
                .number(1l)
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

}
