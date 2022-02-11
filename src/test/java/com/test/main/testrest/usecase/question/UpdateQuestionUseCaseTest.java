package com.test.main.testrest.usecase.question;

import com.test.main.testrest.application.port.in.question.UpdateQuestionCommand;
import com.test.main.testrest.application.port.out.QuestionJpaRepository;
import com.test.main.testrest.application.usecase.question.UpdateQuestionUseCase;
import com.test.main.testrest.config.TestConfig;
import com.test.main.testrest.config.exception.ErrorCode;
import com.test.main.testrest.config.exception.GenericException;
import com.test.main.testrest.domain.Question;
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
public class UpdateQuestionUseCaseTest {

    private final Long QUIZ_ID = 1l;
    private final Long QUESTION_ID = 1l;
    private final Long number = 1l;
    private final QuestionJpaRepository questionJpaRepository = mock(QuestionJpaRepository.class);
    private final UpdateQuestionUseCase useCase = new UpdateQuestionUseCase(questionJpaRepository);

    @Test
    @DisplayName("Cuando intento actualizar una pregunta correctamente, entonces regreso la pregunta creada")
    public void executeOk() {
        UpdateQuestionCommand.QuestionCommand command = getCommandMock();
        Question expected = getQuestionMock();

        when(questionJpaRepository.update(eq(expected))).thenReturn(expected);

        Question result = useCase.execute(command);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);

        verify(questionJpaRepository, times(1)).update(eq(expected));
    }

    @Test
    @DisplayName("Cuando hay un error al intentar actualizar una pregunta, entonces lanzo una excepcion")
    public void executeError() {
        UpdateQuestionCommand.QuestionCommand command = getCommandMock();
        Question expected = getQuestionMock();

        when(questionJpaRepository.update(eq(expected))).thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));

        Throwable throwable = catchThrowable(() -> useCase.execute(command));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(questionJpaRepository, times(1)).update(eq(expected));
    }

    public UpdateQuestionCommand.QuestionCommand getCommandMock() {
        return UpdateQuestionCommand.QuestionCommand.builder()
                .quizId(1l)
                .number(1l)
                .description("DESCRIPTION")
                .type(TypeOption.OPEN)
                .build();
    }

    public Question getQuestionMock() {
        return Question.builder()
                .quizId(1l)
                .number(1l)
                .description("DESCRIPTION")
                .type(TypeOption.OPEN)
                .build();
    }

}
