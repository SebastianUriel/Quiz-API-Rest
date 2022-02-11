package com.test.main.testrest.usecase.question;

import com.test.main.testrest.application.port.out.QuestionJpaRepository;
import com.test.main.testrest.application.usecase.question.GetQuestionUseCase;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Import(TestConfig.class)
public class GetQuestionUseCaseTest {

    private final Long QUIZ_ID = 1l;
    private final QuestionJpaRepository questionJpaRepository = mock(QuestionJpaRepository.class);
    private final GetQuestionUseCase useCase = new GetQuestionUseCase(questionJpaRepository);

    @Test
    @DisplayName("Cuando intento obtener preguntas, entonces regreso la lista de preguntas")
    public void executeOk() {
        List<Question> expected = getQuestionMock();

        when(questionJpaRepository.get(eq(QUIZ_ID))).thenReturn(expected);

        List<Question> result = useCase.execute(QUIZ_ID);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);

        verify(questionJpaRepository, times(1)).get(eq(QUIZ_ID));
    }

    @Test
    @DisplayName("Cuando hay un error al intentar obtener las preguntas, entonces lanzo una excepcion")
    public void executeError() {
        when(questionJpaRepository.get(eq(QUIZ_ID))).thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));

        Throwable throwable = catchThrowable(() -> useCase.execute(QUIZ_ID));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(questionJpaRepository, times(1)).get(eq(QUIZ_ID));
    }


    public List<Question> getQuestionMock() {
        return List.of(Question.builder()
                .quizId(1l)
                .number(1l)
                .description("DESCRIPTION")
                .type(TypeOption.OPEN)
                .build());
    }

}
