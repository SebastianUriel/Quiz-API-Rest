package com.test.main.testrest.adapter.jpa;

import com.test.main.testrest.adapter.jpa.entity.QuizEntity;
import com.test.main.testrest.adapter.jpa.repository.QuizRepository;
import com.test.main.testrest.config.TestConfig;
import com.test.main.testrest.config.exception.ErrorCode;
import com.test.main.testrest.config.exception.GenericException;
import com.test.main.testrest.domain.Quiz;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.util.Date;
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
public class QuizJpaRepositoryAdapterTest {

    private final Long QUIZ_ID = 1l;
    private final QuizRepository quizRepository = mock(QuizRepository.class);
    private final QuizJpaRepositoryAdapter jpaRepositoryAdapter = new QuizJpaRepositoryAdapter(quizRepository);

    @Test
    @DisplayName("Cuando obtengo informacion desde la base de datos, entonces regreso un listado")
    public void getAllOk() {
        List<Quiz> expected = getQuizzesMock();
        List<QuizEntity> entities = getQuizEntitiesMock();

        when(quizRepository.findByAvailable(eq(true)))
                .thenReturn(entities);

        List<Quiz> result = jpaRepositoryAdapter.getAll();

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);

        verify(quizRepository, times(1)).findByAvailable(eq(true));
    }

    @Test
    @DisplayName("CUando hay un error al momento de obtener informacion, entonces lanzo una excepcion")
    public void getAllError() {
        when(quizRepository.findByAvailable(eq(true)))
                .thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));

        Throwable throwable = catchThrowable(() -> jpaRepositoryAdapter.getAll());

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(quizRepository, times(1)).findByAvailable(eq(true));
    }

    @Test
    @DisplayName("Cuando obtengo informacion desde la base de datos, entonces regreso un listado")
    public void getOk() {
        Quiz expected = getQuizMock();
        List<QuizEntity> entities = getQuizEntitiesMock();

        when(quizRepository.findByIdAndAvailable(eq(QUIZ_ID), eq(true)))
                .thenReturn(entities);

        Quiz result = jpaRepositoryAdapter.get(QUIZ_ID);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);

        verify(quizRepository, times(1))
                .findByIdAndAvailable(eq(QUIZ_ID), eq(true));
    }

    @Test
    @DisplayName("CUando hay un error al momento de obtener informacion, entonces lanzo una excepcion")
    public void getError() {
        when(quizRepository.findByIdAndAvailable(eq(QUIZ_ID), eq(true)))
                .thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));

        Throwable throwable = catchThrowable(() -> jpaRepositoryAdapter.get(QUIZ_ID));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(quizRepository, times(1))
                .findByIdAndAvailable(eq(QUIZ_ID), eq(true));
    }

    @Test
    @DisplayName("Cuando se crea un nuevo registro correctamente, entonces regreso el registro creado")
    public void createOk() {
        Quiz expected = getQuizMock();
        QuizEntity entity = getQuizEntityMock();

        when(quizRepository.save(any(QuizEntity.class))).thenReturn(entity);

        Quiz result = jpaRepositoryAdapter.create(expected);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);

        verify(quizRepository, times(1)).save(any(QuizEntity.class));
    }

    @Test
    @DisplayName("Cuando hay un error al crear un registro, entonces lanzo una excepcion")
    public void createError() {
        Quiz expected = getQuizMock();
        QuizEntity entity = getQuizEntityMock();

        when(quizRepository.save(any(QuizEntity.class)))
                .thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));

        Throwable throwable = catchThrowable(() -> jpaRepositoryAdapter.create(expected));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(quizRepository, times(1)).save(any(QuizEntity.class));
    }

    @Test
    @DisplayName("Cuando actualizo un registro correctamente, entonces regreso el registro actualizado")
    public void updateOk() {
        Quiz expected = getQuizMock();
        QuizEntity entity = getQuizEntityMock();

        when(quizRepository.save(any(QuizEntity.class))).thenReturn(entity);
        when(quizRepository.existsById(eq(entity.getId()))).thenReturn(true);

        Quiz result = jpaRepositoryAdapter.update(expected);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);

        verify(quizRepository, times(1)).save(any(QuizEntity.class));
        verify(quizRepository, times(1)).existsById(eq(entity.getId()));
    }

    @Test
    @DisplayName("Cuando hay un error al actualizar un registro, entonces lanzo una excepcion")
    public void updateError() {
        Quiz expected = getQuizMock();
        QuizEntity entity = getQuizEntityMock();

        when(quizRepository.save(any(QuizEntity.class)))
                .thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));
        when(quizRepository.existsById(eq(entity.getId()))).thenReturn(true);

        Throwable throwable = catchThrowable(() -> jpaRepositoryAdapter.update(expected));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(quizRepository, times(1)).save(any(QuizEntity.class));
        verify(quizRepository, times(1)).existsById(eq(entity.getId()));
    }

    @Test
    @DisplayName("Cuando se elimina correctamente un registro, entonces no lanzo ninguna excepcion")
    public void deleteOk() {
        QuizEntity entity = getQuizEntityMock();

        when(quizRepository.findById(eq(entity.getId()))).thenReturn(Optional.ofNullable(entity));
        when(quizRepository.save(eq(entity))).thenReturn(entity);

        jpaRepositoryAdapter.delete(QUIZ_ID);

        verify(quizRepository, times(1)).findById(eq(entity.getId()));
        verify(quizRepository, times(1)).save(eq(entity));
    }

    @Test
    @DisplayName("Cuando hay un error al eliminar un registro, entonces lanzo una excepcion")
    public void deleteError() {
        QuizEntity entity = getQuizEntityMock();

        when(quizRepository.findById(eq(entity.getId()))).thenReturn(Optional.ofNullable(entity));
        when(quizRepository.save(eq(entity))).thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));

        Throwable throwable = catchThrowable(() -> jpaRepositoryAdapter.delete(QUIZ_ID));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(quizRepository, times(1)).findById(eq(entity.getId()));
        verify(quizRepository, times(1)).save(eq(entity));
    }

    public Quiz getQuizMock() {
        return Quiz.builder()
                .id(1l)
                .title("TITLE")
                .description("DESCRIPTION")
                .build();
    }

    public List<Quiz> getQuizzesMock() {
        return List.of(Quiz.builder()
                .id(1l)
                .title("TITLE")
                .description("DESCRIPTION")
                .build());
    }

    public QuizEntity getQuizEntityMock() {
        return QuizEntity.builder()
                .id(1l)
                .title("TITLE")
                .description("DESCRIPTION")
                .createAt(new Date())
                .lastUpdate(new Date())
                .available(true)
                .build();
    }

    public List<QuizEntity> getQuizEntitiesMock() {
        return List.of(QuizEntity.builder()
                .id(1l)
                .title("TITLE")
                .description("DESCRIPTION")
                .createAt(new Date())
                .lastUpdate(new Date())
                .available(true)
                .build());
    }

}
