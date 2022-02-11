package com.test.main.testrest.usecase.quiz;

import com.test.main.testrest.application.port.out.QuizJpaRepository;
import com.test.main.testrest.application.usecase.quiz.DeleteQuizUseCase;
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
public class DeleteQuizUseCaseTest {

    private final Long QUIZ_ID = 1l;
    private final QuizJpaRepository quizJpaRepository = mock(QuizJpaRepository.class);
    private final DeleteQuizUseCase useCase = new DeleteQuizUseCase(quizJpaRepository);

    @Test
    @DisplayName("Cuando intento eliminar un quiz correctamente, entonces no lanzo ninguna excepcion")
    public void executeOk() {
        doNothing().when(quizJpaRepository).delete(eq(QUIZ_ID));

        useCase.execute(QUIZ_ID);

        verify(quizJpaRepository, times(1)).delete(eq(QUIZ_ID));
    }

    @Test
    @DisplayName("Cuando hay un error al intentar eliminar un quiz, entonces lanzo una excepcion")
    public void executeError() {
        doThrow(new GenericException(ErrorCode.DATABASE_ERROR))
                .when(quizJpaRepository).delete(eq(QUIZ_ID));

        Throwable throwable = catchThrowable(() -> useCase.execute(QUIZ_ID));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(quizJpaRepository, times(1)).delete(eq(QUIZ_ID));
    }

}
