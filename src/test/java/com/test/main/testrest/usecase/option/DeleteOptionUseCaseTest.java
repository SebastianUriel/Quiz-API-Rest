package com.test.main.testrest.usecase.option;

import com.test.main.testrest.application.port.out.OptionJpaRepository;
import com.test.main.testrest.application.usecase.option.DeleteOptionUseCase;
import com.test.main.testrest.config.TestConfig;
import com.test.main.testrest.config.exception.ErrorCode;
import com.test.main.testrest.config.exception.GenericException;
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
public class DeleteOptionUseCaseTest {

    private final Long QUIZ_ID = 1l;
    private final Long QUESTION_ID = 1l;
    private final Long NUMBER = 1l;
    private final OptionJpaRepository optionJpaRepository = mock(OptionJpaRepository.class);
    private final DeleteOptionUseCase useCase = new DeleteOptionUseCase(optionJpaRepository);

    @Test
    @DisplayName("Cuando intento eliminar una opcion correctamente, entonces no lanzo ninguna excepcion")
    public void executeOk() {
        doNothing().when(optionJpaRepository).delete(eq(QUIZ_ID), eq(QUESTION_ID), eq(NUMBER));

        useCase.execute(QUIZ_ID, QUESTION_ID, NUMBER);

        verify(optionJpaRepository, times(1)).delete(eq(QUIZ_ID), eq(QUESTION_ID), eq(NUMBER));
    }

    @Test
    @DisplayName("Cuando hay un error al intentar eliminar una opcion, entonces lanzo una excepcion")
    public void executeError() {
        doThrow(new GenericException(ErrorCode.DATABASE_ERROR))
                .when(optionJpaRepository).delete(eq(QUIZ_ID), eq(QUESTION_ID), eq(NUMBER));

        Throwable throwable = catchThrowable(() -> useCase.execute(QUIZ_ID, QUESTION_ID, NUMBER));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(optionJpaRepository, times(1)).delete(eq(QUIZ_ID), eq(QUESTION_ID), eq(NUMBER));
    }

}
