package com.test.main.testrest.usecase.option;

import com.test.main.testrest.application.port.in.option.CreateOptionCommand;
import com.test.main.testrest.application.port.out.OptionJpaRepository;
import com.test.main.testrest.application.usecase.option.GetOptionUseCase;
import com.test.main.testrest.config.TestConfig;
import com.test.main.testrest.config.exception.ErrorCode;
import com.test.main.testrest.config.exception.GenericException;
import com.test.main.testrest.domain.Option;
import com.test.main.testrest.domain.Question;
import com.test.main.testrest.domain.TypeOption;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@Import(TestConfig.class)
public class GetOptionUseCaseTest {

    private final Long QUIZ_ID = 1l;
    private final Long QUESTION_ID = 1l;
    private final OptionJpaRepository optionJpaRepository = mock(OptionJpaRepository.class);
    private final GetOptionUseCase useCase = new GetOptionUseCase(optionJpaRepository);

    @Test
    @DisplayName("Cuando intento obtener opciones, entonces regreso la lista de opciones")
    public void executeOk() {
        List<Option> expected = getOptionMock();

        when(optionJpaRepository.get(eq(QUIZ_ID), eq(QUESTION_ID))).thenReturn(expected);

        List<Option> result = useCase.execute(QUIZ_ID, QUESTION_ID);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);

        verify(optionJpaRepository, times(1)).get(eq(QUIZ_ID), eq(QUESTION_ID));
    }

    @Test
    @DisplayName("Cuando hay un error al intentar obtener las opciones, entonces lanzo una excepcion")
    public void executeError() {
        when(optionJpaRepository.get(eq(QUIZ_ID), eq(QUESTION_ID))).thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));

        Throwable throwable = catchThrowable(() -> useCase.execute(QUIZ_ID, QUESTION_ID));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(optionJpaRepository, times(1)).get(eq(QUIZ_ID), eq(QUESTION_ID));
    }

    public List<Option> getOptionMock() {
        return List.of(Option.builder()
                .quizId(1l)
                .questionId(1l)
                .number(1l)
                .description("DESCRIPTION")
                .build());
    }

}
