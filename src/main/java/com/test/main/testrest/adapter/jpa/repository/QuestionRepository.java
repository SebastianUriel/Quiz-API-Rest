package com.test.main.testrest.adapter.jpa.repository;

import com.test.main.testrest.adapter.jpa.entity.QuestionEntity;
import com.test.main.testrest.adapter.jpa.entity.id.QuestionId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface QuestionRepository extends CrudRepository<QuestionEntity, QuestionId> {

    List<QuestionEntity> findByQuestionIdQuizIdAndAvailable(Long quizId, Boolean available);

    @Query(value = "SELECT max(number) FROM question WHERE quiz_id = ?1", nativeQuery = true)
    BigDecimal nextNumber(Long quizId);

}
