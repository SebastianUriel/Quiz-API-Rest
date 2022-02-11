package com.test.main.testrest.adapter.jpa;

import com.test.main.testrest.adapter.jpa.entity.OptionEntity;
import com.test.main.testrest.adapter.jpa.entity.id.OptionId;
import com.test.main.testrest.adapter.jpa.exception.DatabaseException;
import com.test.main.testrest.adapter.jpa.repository.OptionRepository;
import com.test.main.testrest.application.port.out.OptionJpaRepository;
import com.test.main.testrest.config.exception.ErrorCode;
import com.test.main.testrest.domain.Option;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OptionJpaRepositoryAdapter implements OptionJpaRepository {

    private final OptionRepository optionRepository;

    public OptionJpaRepositoryAdapter(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    @Override
    public List<Option> get(Long quizId, Long questionId) {
        try {
            List<OptionEntity> options = this.optionRepository.findByOptionIdQuizIdAndOptionIdQuestionIdAndAvailable(quizId, questionId, true);
            return options.stream()
                    .map(OptionEntity::toDomain)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.info("Error in OptionJpaRepository get= {}", ex);
            throw new DatabaseException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public Option create(Option option) {
        try {
            OptionEntity entity = OptionEntity.fromDomain(option);
            entity = this.optionRepository.save(entity);
            return entity.toDomain();
        } catch (Exception ex) {
            log.info("Error in OptionJpaRepository create= {}", ex);
            throw new DatabaseException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public Option update(Option option) {
        try {
            OptionEntity entity = OptionEntity.fromDomain(option);
            if(this.optionRepository.existsById(entity.getOptionId())) {
                entity = this.optionRepository.save(entity);
            }
            return entity.toDomain();
        } catch (Exception ex) {
            log.info("Error in OptionJpaRepository update= {}", ex);
            throw new DatabaseException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public void delete(Long quizId, Long questionId, Long number) {
        try {
            OptionEntity entity = this.optionRepository.findById(OptionId.builder()
                    .quizId(quizId)
                    .questionId(questionId)
                    .number(number)
                    .build()).orElse(null);

            Optional.ofNullable(entity).ifPresent(value -> {
                value.setAvailable(false);
                this.optionRepository.save(value);
            });
        } catch (Exception ex) {
            log.info("Error in OptionJpaRepository get= {}", ex);
            throw new DatabaseException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public BigDecimal nextNumber(Long quizId, Long questionId) {
        try {
            BigDecimal number = this.optionRepository.nextNumber(quizId, questionId);
            return Optional.ofNullable(number)
                    .map(value -> value.add(new BigDecimal(1)))
                    .orElse(new BigDecimal(1));
        } catch (Exception ex) {
            log.info("Error in OptionJpaRepository nextNumber= {}", ex);
            throw new DatabaseException(ErrorCode.DATABASE_ERROR);
        }
    }
}
