package com.test.main.testrest.usecase.question;

import com.test.main.testrest.application.port.in.question.CreateQuestionCommand;
import com.test.main.testrest.application.port.out.QuestionJpaRepository;
import com.test.main.testrest.application.usecase.question.CreateQuestionUseCase;
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

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@Import(TestConfig.class)
public class CreateQuestionUseCaseTest {

    private final Long QUIZ_ID = 1l;
    private final QuestionJpaRepository questionJpaRepository = mock(QuestionJpaRepository.class);
    private final CreateQuestionUseCase useCase = new CreateQuestionUseCase(questionJpaRepository);

    @Test
    @DisplayName("Cuando intento crear una pregunta correctamente, entonces regreso la opcion creada")
    public void executeOk() {
        CreateQuestionCommand.QuestionCommand command = getCommandMock();
        Question expected = getQuestionMock();
        BigDecimal nextNumber = getNextNumberMock();

        when(questionJpaRepository.nextNumber(eq(QUIZ_ID))).thenReturn(nextNumber);
        when(questionJpaRepository.create(eq(expected))).thenReturn(expected);

        Question result = useCase.execute(command);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);

        verify(questionJpaRepository, times(1)).nextNumber(eq(QUIZ_ID));
        verify(questionJpaRepository, times(1)).create(eq(expected));
    }

    @Test
    @DisplayName("Cuando hay un error al intentar crear una pregunta, entonces lanzo una excepcion")
    public void executeError() {
        CreateQuestionCommand.QuestionCommand command = getCommandMock();
        Question expected = getQuestionMock();
        BigDecimal nextNumber = getNextNumberMock();

        when(questionJpaRepository.nextNumber(eq(QUIZ_ID))).thenReturn(nextNumber);
        when(questionJpaRepository.create(eq(expected))).thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));

        Throwable throwable = catchThrowable(() -> useCase.execute(command));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(questionJpaRepository, times(1)).nextNumber(eq(QUIZ_ID));
        verify(questionJpaRepository, times(1)).create(eq(expected));
    }

    public CreateQuestionCommand.QuestionCommand getCommandMock() {
        return CreateQuestionCommand.QuestionCommand.builder()
                .quizId(1l)
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

    public BigDecimal getNextNumberMock() {
        return new BigDecimal(1l);
    }

}
