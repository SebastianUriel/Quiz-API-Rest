package com.test.main.testrest.usecase.quiz;

import com.test.main.testrest.application.port.in.quiz.CreateQuizCommand;
import com.test.main.testrest.application.port.out.QuizJpaRepository;
import com.test.main.testrest.application.usecase.quiz.CreateQuizUseCase;
import com.test.main.testrest.config.TestConfig;
import com.test.main.testrest.config.exception.ErrorCode;
import com.test.main.testrest.config.exception.GenericException;
import com.test.main.testrest.domain.Quiz;
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
public class CreateQuizUseCaseTest {

    private final QuizJpaRepository quizJpaRepository = mock(QuizJpaRepository.class);
    private final CreateQuizUseCase useCase = new CreateQuizUseCase(quizJpaRepository);

    @Test
    @DisplayName("Cuando intento crear un quiz correctamente, entonces regreso la opcion creada")
    public void executeOk() {
        CreateQuizCommand.QuizCommand command = getCommandMock();
        Quiz expected = getQuizMock();

        when(quizJpaRepository.create(eq(expected))).thenReturn(expected);

        Quiz result = useCase.execute(command);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);

        verify(quizJpaRepository, times(1)).create(eq(expected));
    }

    @Test
    @DisplayName("Cuando hay un error al intentar crear un quiz, entonces lanzo una excepcion")
    public void executeError() {
        CreateQuizCommand.QuizCommand command = getCommandMock();
        Quiz expected = getQuizMock();

        when(quizJpaRepository.create(eq(expected))).thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));

        Throwable throwable = catchThrowable(() -> useCase.execute(command));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(quizJpaRepository, times(1)).create(eq(expected));
    }

    public CreateQuizCommand.QuizCommand getCommandMock() {
        return CreateQuizCommand.QuizCommand.builder()
                .title("TITLE")
                .description("DESCRIPTION")
                .build();
    }

    public Quiz getQuizMock() {
        return Quiz.builder()
                .title("TITLE")
                .description("DESCRIPTION")
                .build();
    }

}
