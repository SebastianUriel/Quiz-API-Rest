package com.test.main.testrest.adapter.jpa;

import com.test.main.testrest.adapter.jpa.entity.QuestionEntity;
import com.test.main.testrest.adapter.jpa.entity.QuizEntity;
import com.test.main.testrest.adapter.jpa.entity.id.QuestionId;
import com.test.main.testrest.adapter.jpa.exception.DatabaseException;
import com.test.main.testrest.adapter.jpa.repository.QuestionRepository;
import com.test.main.testrest.adapter.jpa.repository.QuizRepository;
import com.test.main.testrest.application.port.out.QuestionJpaRepository;
import com.test.main.testrest.config.exception.ErrorCode;
import com.test.main.testrest.domain.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class QuestionJpaRepositoryAdapter implements QuestionJpaRepository {

    private final QuestionRepository questionRepository;

    public QuestionJpaRepositoryAdapter(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public List<Question> get(Long quizId) {
        try {
            List<QuestionEntity> questions = this.questionRepository.findByQuestionIdQuizIdAndAvailable(quizId, true);
            return questions.stream()
                    .map(QuestionEntity::toDomain)
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            log.info("Error in QuestionJpaRepository get= {}", ex);
            throw new DatabaseException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public Question create(Question question) {
        try {
            QuestionEntity entity = QuestionEntity.fromDomain(question);
            entity = this.questionRepository.save(entity);
            return entity.toDomain();
        } catch (Exception ex) {
            log.info("Error in QuestionJpaRepository create= {}", ex);
            throw new DatabaseException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public Question update(Question question) {
        try {
            QuestionEntity entity = QuestionEntity.fromDomain(question);
            if(this.questionRepository.existsById(entity.getQuestionId())) {
                entity = this.questionRepository.save(entity);
            }
            return entity.toDomain();
        } catch (Exception ex) {
            log.info("Error in QuestionJpaRepository update= {}", ex);
            throw new DatabaseException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public void delete(Long quizId, Long number) {
        try {
            QuestionEntity entity = this.questionRepository.findById(QuestionId.builder()
                    .quizId(quizId)
                    .number(number)
                    .build()).orElse(null);
            Optional.ofNullable(entity).ifPresent(value -> {
                value.setAvailable(false);
                this.questionRepository.save(value);
            });
        } catch (Exception ex) {
            log.info("Error in QuestionJpaRepository delete= {}", ex);
            throw new DatabaseException(ErrorCode.DATABASE_ERROR);
        }
    }

    @Override
    public BigDecimal nextNumber(Long quizId) {
        try {
            BigDecimal number = this.questionRepository.nextNumber(quizId);
            return Optional.ofNullable(number)
                    .map(value -> value.add(new BigDecimal(1)))
                    .orElse(new BigDecimal(1));
        } catch (Exception ex) {
            log.info("Error in QuestionJpaRepository nextNumber= {}", ex);
            throw new DatabaseException(ErrorCode.DATABASE_ERROR);
        }
    }

}
