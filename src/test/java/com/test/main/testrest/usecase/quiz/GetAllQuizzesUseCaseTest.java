package com.test.main.testrest.usecase.quiz;

import com.test.main.testrest.application.port.out.OptionJpaRepository;
import com.test.main.testrest.application.port.out.QuestionJpaRepository;
import com.test.main.testrest.application.port.out.QuizJpaRepository;
import com.test.main.testrest.application.usecase.quiz.GetAllQuizzesUseCase;
import com.test.main.testrest.application.usecase.quiz.GetQuizUseCase;
import com.test.main.testrest.config.TestConfig;
import com.test.main.testrest.config.exception.ErrorCode;
import com.test.main.testrest.config.exception.GenericException;
import com.test.main.testrest.domain.Option;
import com.test.main.testrest.domain.Question;
import com.test.main.testrest.domain.Quiz;
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
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@Import(TestConfig.class)
public class GetAllQuizzesUseCaseTest {

    private final Long QUIZ_ID = 1l;
    private final QuizJpaRepository quizJpaRepository = mock(QuizJpaRepository.class);
    public final QuestionJpaRepository questionJpaRepository = mock(QuestionJpaRepository.class);
    public final OptionJpaRepository optionJpaRepository = mock(OptionJpaRepository.class);
    private final GetAllQuizzesUseCase useCase = new GetAllQuizzesUseCase(quizJpaRepository, questionJpaRepository, optionJpaRepository);

    @Test
    @DisplayName("Cuando intento obtener quiz, entonces regreso la lista de quiz")
    public void executeOk() {
        List<Quiz> quiz = getQuizMock();
        List<Question> questions = getQuestionMock();
        List<Option> options = getOptionMock();

        when(quizJpaRepository.getAll()).thenReturn(quiz);
        when(questionJpaRepository.get(eq(QUIZ_ID))).thenReturn(questions);
        when(optionJpaRepository.get(eq(QUIZ_ID), eq(questions.get(0).getNumber()))).thenReturn(options);

        List<Quiz> result = useCase.execute();

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(quiz);

        verify(quizJpaRepository, times(1)).getAll();
        verify(questionJpaRepository, times(1)).get(eq(QUIZ_ID));
        verify(optionJpaRepository, times(1)).get(eq(QUIZ_ID), eq(questions.get(0).getNumber()));
    }

    @Test
    @DisplayName("Cuando hay un error al intentar obtener las quiz, entonces lanzo una excepcion")
    public void executeError() {
        List<Quiz> quiz = getQuizMock();
        List<Question> questions = getQuestionMock();
        List<Option> options = getOptionMock();

        when(quizJpaRepository.getAll()).thenReturn(quiz);
        when(questionJpaRepository.get(eq(QUIZ_ID))).thenReturn(questions);
        when(optionJpaRepository.get(eq(QUIZ_ID), eq(questions.get(0).getNumber()))).thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));

        Throwable throwable = catchThrowable(() -> useCase.execute());

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(quizJpaRepository, times(1)).getAll();
        verify(questionJpaRepository, times(1)).get(eq(QUIZ_ID));
        verify(optionJpaRepository, times(1)).get(eq(QUIZ_ID), eq(questions.get(0).getNumber()));
    }

    public List<Quiz> getQuizMock() {
        return List.of(Quiz.builder()
                .id(1l)
                .title("TITLE")
                .description("DESCRIPTION")
                .questions(getQuestionMock())
                .build());
    }

    public List<Question> getQuestionMock() {
        return List.of(Question.builder()
                .quizId(1l)
                .number(1l)
                .description("DESCRIPTION")
                .type(TypeOption.OPEN)
                .options(getOptionMock())
                .build());
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
