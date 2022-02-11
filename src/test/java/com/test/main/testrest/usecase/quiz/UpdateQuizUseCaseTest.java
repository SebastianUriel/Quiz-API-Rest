package com.test.main.testrest.usecase.quiz;

import com.test.main.testrest.application.port.in.quiz.UpdateQuizCommand;
import com.test.main.testrest.application.port.out.QuizJpaRepository;
import com.test.main.testrest.application.usecase.quiz.UpdateQuizUseCase;
import com.test.main.testrest.config.TestConfig;
import com.test.main.testrest.config.exception.ErrorCode;
import com.test.main.testrest.config.exception.GenericException;
import com.test.main.testrest.domain.Quiz;
import com.test.main.testrest.domain.TypeOption;
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
public class UpdateQuizUseCaseTest {

    private final QuizJpaRepository quizJpaRepository = mock(QuizJpaRepository.class);
    private final UpdateQuizUseCase useCase = new UpdateQuizUseCase(quizJpaRepository);

    @Test
    @DisplayName("Cuando intento actualizar un quiz correctamente, entonces regreso el quiz creado")
    public void executeOk() {
        UpdateQuizCommand.QuizCommand command = getCommandMock();
        Quiz expected = getQuizMock();

        when(quizJpaRepository.update(eq(expected))).thenReturn(expected);

        Quiz result = useCase.execute(command);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);

        verify(quizJpaRepository, times(1)).update(eq(expected));
    }

    @Test
    @DisplayName("Cuando hay un error al intentar actualizar un quiz, entonces lanzo una excepcion")
    public void executeError() {
        UpdateQuizCommand.QuizCommand command = getCommandMock();
        Quiz expected = getQuizMock();

        when(quizJpaRepository.update(eq(expected))).thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));

        Throwable throwable = catchThrowable(() -> useCase.execute(command));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(quizJpaRepository, times(1)).update(eq(expected));
    }

    public UpdateQuizCommand.QuizCommand getCommandMock() {
        return UpdateQuizCommand.QuizCommand.builder()
                .id(1l)
                .description("DESCRIPTION")
                .title("TITLE")
                .build();
    }

    public Quiz getQuizMock() {
        return Quiz.builder()
                .id(1l)
                .description("DESCRIPTION")
                .title("TITLE")
                .build();
    }
    
}
