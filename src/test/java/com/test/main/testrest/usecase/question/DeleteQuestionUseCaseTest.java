package com.test.main.testrest.usecase.question;

import com.test.main.testrest.application.port.out.QuestionJpaRepository;
import com.test.main.testrest.application.usecase.question.DeleteQuestionUseCase;
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

@ExtendWith(MockitoExtension.class)
@Import(TestConfig.class)
public class DeleteQuestionUseCaseTest {

    private final Long QUIZ_ID = 1l;
    private final Long NUMBER = 1l;
    private final QuestionJpaRepository questionJpaRepository = mock(QuestionJpaRepository.class);
    private final DeleteQuestionUseCase useCase = new DeleteQuestionUseCase(questionJpaRepository);

    @Test
    @DisplayName("Cuando intento eliminar una pregunta correctamente, entonces no lanzo ninguna excepcion")
    public void executeOk() {
        doNothing().when(questionJpaRepository).delete(eq(QUIZ_ID), eq(NUMBER));

        useCase.execute(QUIZ_ID, NUMBER);

        verify(questionJpaRepository, times(1)).delete(eq(QUIZ_ID), eq(NUMBER));
    }

    @Test
    @DisplayName("Cuando hay un error al intentar eliminar una pregunta, entonces lanzo una excepcion")
    public void executeError() {
        doThrow(new GenericException(ErrorCode.DATABASE_ERROR))
                .when(questionJpaRepository).delete(eq(QUIZ_ID), eq(NUMBER));

        Throwable throwable = catchThrowable(() -> useCase.execute(QUIZ_ID, NUMBER));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(questionJpaRepository, times(1)).delete(eq(QUIZ_ID), eq(NUMBER));
    }

}
