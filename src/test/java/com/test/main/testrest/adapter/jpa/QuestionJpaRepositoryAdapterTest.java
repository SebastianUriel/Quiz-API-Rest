package com.test.main.testrest.adapter.jpa;

import com.test.main.testrest.adapter.jpa.entity.QuestionEntity;
import com.test.main.testrest.adapter.jpa.entity.id.QuestionId;
import com.test.main.testrest.adapter.jpa.repository.QuestionRepository;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@Import(TestConfig.class)
public class QuestionJpaRepositoryAdapterTest {

    private final Long QUIZ_ID = 1l;
    private final Long NUMBER = 1l;
    private final QuestionRepository questionRepository = mock(QuestionRepository.class);
    private final QuestionJpaRepositoryAdapter jpaRepositoryAdapter = new QuestionJpaRepositoryAdapter(questionRepository);

    @Test
    @DisplayName("Cuando obtengo informacion desde la base de datos, entonces regreso un listado")
    public void getOk() {
        List<Question> expected = getQuestionsMock();
        List<QuestionEntity> entities = getQuestionEntitiesMock();

        when(questionRepository.findByQuestionIdQuizIdAndAvailable(eq(QUIZ_ID), eq(true)))
                .thenReturn(entities);

        List<Question> result = jpaRepositoryAdapter.get(QUIZ_ID);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);

        verify(questionRepository, times(1))
                .findByQuestionIdQuizIdAndAvailable(eq(QUIZ_ID), eq(true));
    }

    @Test
    @DisplayName("CUando hay un error al momento de obtener informacion, entonces lanzo una excepcion")
    public void getError() {
        when(questionRepository.findByQuestionIdQuizIdAndAvailable(eq(QUIZ_ID), eq(true)))
                .thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));

        Throwable throwable = catchThrowable(() -> jpaRepositoryAdapter.get(QUIZ_ID));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(questionRepository, times(1))
                .findByQuestionIdQuizIdAndAvailable(eq(QUIZ_ID), eq(true));
    }

    @Test
    @DisplayName("Cuando se crea un nuevo registro correctamente, entonces regreso el registro creado")
    public void createOk() {
        Question expected = getQuestionMock();
        QuestionEntity entity = getQuestionEntityMock();

        when(questionRepository.save(any(QuestionEntity.class))).thenReturn(entity);

        Question result = jpaRepositoryAdapter.create(expected);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);

        verify(questionRepository, times(1)).save(any(QuestionEntity.class));
    }

    @Test
    @DisplayName("Cuando hay un error al crear un registro, entonces lanzo una excepcion")
    public void createError() {
        Question expected = getQuestionMock();
        QuestionEntity entity = getQuestionEntityMock();

        when(questionRepository.save(any(QuestionEntity.class)))
                .thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));

        Throwable throwable = catchThrowable(() -> jpaRepositoryAdapter.create(expected));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(questionRepository, times(1)).save(any(QuestionEntity.class));
    }

    @Test
    @DisplayName("Cuando actualizo un registro correctamente, entonces regreso el registro actualizado")
    public void updateOk() {
        Question expected = getQuestionMock();
        QuestionEntity entity = getQuestionEntityMock();

        when(questionRepository.save(any(QuestionEntity.class))).thenReturn(entity);
        when(questionRepository.existsById(eq(entity.getQuestionId()))).thenReturn(true);

        Question result = jpaRepositoryAdapter.update(expected);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);

        verify(questionRepository, times(1)).save(any(QuestionEntity.class));
        verify(questionRepository, times(1)).existsById(eq(entity.getQuestionId()));
    }

    @Test
    @DisplayName("Cuando hay un error al actualizar un registro, entonces lanzo una excepcion")
    public void updateError() {
        Question expected = getQuestionMock();
        QuestionEntity entity = getQuestionEntityMock();

        when(questionRepository.save(any(QuestionEntity.class)))
                .thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));
        when(questionRepository.existsById(eq(entity.getQuestionId()))).thenReturn(true);

        Throwable throwable = catchThrowable(() -> jpaRepositoryAdapter.update(expected));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(questionRepository, times(1)).save(any(QuestionEntity.class));
        verify(questionRepository, times(1)).existsById(eq(entity.getQuestionId()));
    }

    @Test
    @DisplayName("Cuando se elimina correctamente un registro, entonces no lanzo ninguna excepcion")
    public void deleteOk() {
        QuestionEntity entity = getQuestionEntityMock();
        QuestionId id = QuestionId.builder()
                .quizId(QUIZ_ID)
                .number(NUMBER)
                .build();

        when(questionRepository.findById(eq(id))).thenReturn(Optional.ofNullable(entity));
        when(questionRepository.save(eq(entity))).thenReturn(entity);

        jpaRepositoryAdapter.delete(QUIZ_ID, NUMBER);

        verify(questionRepository, times(1)).findById(eq(id));
        verify(questionRepository, times(1)).save(eq(entity));
    }

    @Test
    @DisplayName("Cuando hay un error al eliminar un registro, entonces lanzo una excepcion")
    public void deleteError() {
        QuestionEntity entity = getQuestionEntityMock();
        QuestionId id = QuestionId.builder()
                .quizId(QUIZ_ID)
                .number(NUMBER)
                .build();

        when(questionRepository.findById(eq(id))).thenReturn(Optional.ofNullable(entity));
        when(questionRepository.save(eq(entity))).thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));

        Throwable throwable = catchThrowable(() -> jpaRepositoryAdapter.delete(QUIZ_ID, NUMBER));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(questionRepository, times(1)).findById(eq(id));
        verify(questionRepository, times(1)).save(eq(entity));
    }

    @Test
    @DisplayName("Cuando obtengo el numero siguiente correctamente, entoncres regreso el valor")
    public void nextNumberOk() {
        BigDecimal expected = new BigDecimal(1);

        when(questionRepository.nextNumber(eq(QUIZ_ID))).thenReturn(expected);

        BigDecimal result = jpaRepositoryAdapter.nextNumber(QUIZ_ID);

        assertThat(result).isNotNull();

        verify(questionRepository, times(1)).nextNumber(eq(QUIZ_ID));
    }

    @Test
    @DisplayName("Cuando hay un error al obtener el numero siguiente, entonces lanzo una excepcion")
    public void nextNumberError() {

        when(questionRepository.nextNumber(eq(QUIZ_ID)))
                .thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));

        Throwable throwable = catchThrowable(() -> jpaRepositoryAdapter.nextNumber(QUIZ_ID));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(questionRepository, times(1)).nextNumber(eq(QUIZ_ID));
    }

    public Question getQuestionMock() {
        return Question.builder()
                .quizId(1l)
                .number(1l)
                .description("DESCRIPTION")
                .type(TypeOption.OPEN)
                .build();
    }

    public List<Question> getQuestionsMock() {
        return List.of(Question.builder()
                .quizId(1l)
                .number(1l)
                .description("DESCRIPTION")
                .type(TypeOption.OPEN)
                .build());
    }

    public QuestionEntity getQuestionEntityMock() {
        return QuestionEntity.builder()
                .questionId(QuestionId.builder()
                        .quizId(1l)
                        .number(1l)
                        .build())
                .description("DESCRIPTION")
                .type("OPEN")
                .available(true)
                .build();
    }

    public List<QuestionEntity> getQuestionEntitiesMock() {
        return List.of(QuestionEntity.builder()
                .questionId(QuestionId.builder()
                        .quizId(1l)
                        .number(1l)
                        .build())
                .description("DESCRIPTION")
                .type("OPEN")
                .available(true)
                .build());
    }
    
}
