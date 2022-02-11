package com.test.main.testrest.adapter.jpa;

import com.test.main.testrest.adapter.jpa.entity.OptionEntity;
import com.test.main.testrest.adapter.jpa.entity.id.OptionId;
import com.test.main.testrest.adapter.jpa.repository.OptionRepository;
import com.test.main.testrest.config.TestConfig;
import com.test.main.testrest.config.exception.ErrorCode;
import com.test.main.testrest.config.exception.GenericException;
import com.test.main.testrest.domain.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@Import(TestConfig.class)
public class OptionJpaRepositoryAdapterTest {

    private final Long QUIZ_ID = 1l;
    private final Long QUESTION_ID = 1l;
    private final Long NUMBER = 1l;
    private final OptionRepository optionRepository = mock(OptionRepository.class);
    private final OptionJpaRepositoryAdapter jpaRepositoryAdapter = new OptionJpaRepositoryAdapter(optionRepository);

    @Test
    @DisplayName("Cuando obtengo informacion desde la base de datos, entonces regreso un listado")
    public void getOk() {
        List<Option> expected = getOptionsMock();
        List<OptionEntity> entities = getOptionEntitiesMock();

        when(optionRepository.findByOptionIdQuizIdAndOptionIdQuestionIdAndAvailable(eq(QUIZ_ID), eq(QUESTION_ID), eq(true)))
                .thenReturn(entities);

        List<Option> result = jpaRepositoryAdapter.get(QUIZ_ID, QUESTION_ID);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);

        verify(optionRepository, times(1))
                .findByOptionIdQuizIdAndOptionIdQuestionIdAndAvailable(eq(QUIZ_ID), eq(QUESTION_ID), eq(true));
    }

    @Test
    @DisplayName("CUando hay un error al momento de obtener informacion, entonces lanzo una excepcion")
    public void getError() {
        when(optionRepository.findByOptionIdQuizIdAndOptionIdQuestionIdAndAvailable(eq(QUIZ_ID), eq(QUESTION_ID), eq(true)))
                .thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));

        Throwable throwable = catchThrowable(() -> jpaRepositoryAdapter.get(QUIZ_ID, QUESTION_ID));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(optionRepository, times(1))
                .findByOptionIdQuizIdAndOptionIdQuestionIdAndAvailable(eq(QUIZ_ID), eq(QUESTION_ID), eq(true));
    }

    @Test
    @DisplayName("Cuando se crea un nuevo registro correctamente, entonces regreso el registro creado")
    public void createOk() {
        Option expected = getOptionMock();
        OptionEntity entity = getOptionEntityMock();

        when(optionRepository.save(any(OptionEntity.class))).thenReturn(entity);

        Option result = jpaRepositoryAdapter.create(expected);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);

        verify(optionRepository, times(1)).save(any(OptionEntity.class));
    }

    @Test
    @DisplayName("Cuando hay un error al crear un registro, entonces lanzo una excepcion")
    public void createError() {
        Option expected = getOptionMock();
        OptionEntity entity = getOptionEntityMock();

        when(optionRepository.save(any(OptionEntity.class)))
                .thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));

        Throwable throwable = catchThrowable(() -> jpaRepositoryAdapter.create(expected));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(optionRepository, times(1)).save(any(OptionEntity.class));
    }

    @Test
    @DisplayName("Cuando actualizo un registro correctamente, entonces regreso el registro actualizado")
    public void updateOk() {
        Option expected = getOptionMock();
        OptionEntity entity = getOptionEntityMock();

        when(optionRepository.save(any(OptionEntity.class))).thenReturn(entity);
        when(optionRepository.existsById(eq(entity.getOptionId()))).thenReturn(true);

        Option result = jpaRepositoryAdapter.update(expected);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expected);

        verify(optionRepository, times(1)).save(any(OptionEntity.class));
        verify(optionRepository, times(1)).existsById(eq(entity.getOptionId()));
    }

    @Test
    @DisplayName("Cuando hay un error al actualizar un registro, entonces lanzo una excepcion")
    public void updateError() {
        Option expected = getOptionMock();
        OptionEntity entity = getOptionEntityMock();

        when(optionRepository.save(any(OptionEntity.class)))
                .thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));
        when(optionRepository.existsById(eq(entity.getOptionId()))).thenReturn(true);

        Throwable throwable = catchThrowable(() -> jpaRepositoryAdapter.update(expected));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(optionRepository, times(1)).save(any(OptionEntity.class));
        verify(optionRepository, times(1)).existsById(eq(entity.getOptionId()));
    }

    @Test
    @DisplayName("Cuando se elimina correctamente un registro, entonces no lanzo ninguna excepcion")
    public void deleteOk() {
        OptionEntity entity = getOptionEntityMock();
        OptionId id = OptionId.builder()
                .quizId(QUIZ_ID)
                .questionId(QUESTION_ID)
                .number(NUMBER)
                .build();

        when(optionRepository.findById(eq(id))).thenReturn(Optional.ofNullable(entity));
        when(optionRepository.save(eq(entity))).thenReturn(entity);

        jpaRepositoryAdapter.delete(QUIZ_ID, QUESTION_ID, NUMBER);

        verify(optionRepository, times(1)).findById(eq(id));
        verify(optionRepository, times(1)).save(eq(entity));
    }

    @Test
    @DisplayName("Cuando hay un error al eliminar un registro, entonces lanzo una excepcion")
    public void deleteError() {
        OptionEntity entity = getOptionEntityMock();
        OptionId id = OptionId.builder()
                .quizId(QUIZ_ID)
                .questionId(QUESTION_ID)
                .number(NUMBER)
                .build();

        when(optionRepository.findById(eq(id))).thenReturn(Optional.ofNullable(entity));
        when(optionRepository.save(eq(entity))).thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));

        Throwable throwable = catchThrowable(() -> jpaRepositoryAdapter.delete(QUIZ_ID, QUESTION_ID, NUMBER));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(optionRepository, times(1)).findById(eq(id));
        verify(optionRepository, times(1)).save(eq(entity));
    }

    @Test
    @DisplayName("Cuando obtengo el numero siguiente correctamente, entoncres regreso el valor")
    public void nextNumberOk() {
        BigDecimal expected = new BigDecimal(1);

        when(optionRepository.nextNumber(eq(QUIZ_ID), eq(QUESTION_ID)))
                .thenReturn(expected);

        BigDecimal result = jpaRepositoryAdapter.nextNumber(QUIZ_ID, QUESTION_ID);

        assertThat(result).isNotNull();

        verify(optionRepository, times(1)).nextNumber(eq(QUIZ_ID), eq(QUESTION_ID));
    }

    @Test
    @DisplayName("Cuando hay un error al obtener el numero siguiente, entonces lanzo una excepcion")
    public void nextNumberError() {

        when(optionRepository.nextNumber(eq(QUIZ_ID), eq(QUESTION_ID)))
                .thenThrow(new GenericException(ErrorCode.DATABASE_ERROR));

        Throwable throwable = catchThrowable(() -> jpaRepositoryAdapter.nextNumber(QUIZ_ID, QUESTION_ID));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(GenericException.class);

        verify(optionRepository, times(1)).nextNumber(eq(QUIZ_ID), eq(QUESTION_ID));
    }

    public Option getOptionMock() {
        return Option.builder()
                .quizId(1l)
                .questionId(1l)
                .number(1l)
                .description("DESCRIPTION")
                .build();
    }

    public List<Option> getOptionsMock() {
        return List.of(Option.builder()
                .quizId(1l)
                .questionId(1l)
                .number(1l)
                .description("DESCRIPTION")
                .build());
    }

    public OptionEntity getOptionEntityMock() {
        return OptionEntity.builder()
                .optionId(OptionId.builder()
                        .quizId(1l)
                        .questionId(1l)
                        .number(1l)
                        .build())
                .description("DESCRIPTION")
                .available(true)
                .build();
    }

    public List<OptionEntity> getOptionEntitiesMock() {
        return List.of(OptionEntity.builder()
                .optionId(OptionId.builder()
                        .quizId(1l)
                        .questionId(1l)
                        .number(1l)
                        .build())
                .description("DESCRIPTION")
                .available(true)
                .build());
    }

}
