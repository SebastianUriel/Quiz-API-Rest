package com.test.main.testrest.adapter.jpa;

import com.test.main.testrest.adapter.jpa.entity.QuizEntity;
import com.test.main.testrest.adapter.jpa.exception.DatabaseException;
import com.test.main.testrest.adapter.jpa.repository.QuizRepository;
import com.test.main.testrest.application.port.out.QuizJpaRepository;
import com.test.main.testrest.config.exception.ErrorCode;
import com.test.main.testrest.domain.Quiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class QuizJpaRepositoryAdapter implements QuizJpaRepository {

    private final QuizRepository quizRepository;

    public QuizJpaRepositoryAdapter(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    @Override
    public List<Quiz> getAll() {
        try {
            List<QuizEntity> quizzes = this.quizRepository.findByAvailable(true);
            return quizzes.stream()
                    .map(QuizEntity::toDomain)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.info("Error in QuizJpaRepository getALL= {}", ex);
            throw new DatabaseException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public Quiz get(Long id) {
        try {
            return this.quizRepository.findByIdAndAvailable(id, true)
                    .stream()
                    .findFirst()
                    .map(QuizEntity::toDomain)
                    .orElse(null);
        } catch (Exception ex) {
            log.info("Error in QuizJpaRepository get= {}", ex);
            throw new DatabaseException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public Quiz create(Quiz quiz) {
        try {
            QuizEntity entity = QuizEntity.fromDomain(quiz);
            entity = this.quizRepository.save(entity);
            return entity.toDomain();
        } catch (Exception ex) {
            log.info("Error in QuizJpaRepository create= {}", ex);
            throw new DatabaseException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public Quiz update(Quiz quiz) {
        try {
            QuizEntity entity = QuizEntity.fromDomain(quiz);
            if(this.quizRepository.existsById(entity.getId())) {
                entity.setLastUpdate(new Date());
                entity = this.quizRepository.save(entity);
            }
            return entity.toDomain();
        } catch (Exception ex) {
            log.info("Error in QuizJpaRepository update= {}", ex);
            throw new DatabaseException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            QuizEntity entity = this.quizRepository.findById(id).orElse(null);
            Optional.ofNullable(entity).ifPresent(value -> {
                value.setLastUpdate(new Date());
                value.setAvailable(false);
                this.quizRepository.save(value);
            });
        } catch (Exception ex) {
            log.info("Error in QuizJpaRepository delete: {}", ex);
            throw new DatabaseException(ErrorCode.DATABASE_ERROR);
        }
    }

}
