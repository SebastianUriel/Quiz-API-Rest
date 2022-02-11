package com.test.main.testrest.adapter.jpa.repository;

import com.test.main.testrest.adapter.jpa.entity.OptionEntity;
import com.test.main.testrest.adapter.jpa.entity.id.OptionId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OptionRepository extends CrudRepository<OptionEntity, OptionId> {

    List<OptionEntity> findByOptionIdQuizIdAndOptionIdQuestionIdAndAvailable(Long quizId, Long questionId, Boolean available);

    @Query(value = "SELECT max(number) FROM qn_option WHERE quiz_id = ?1 AND question_id = ?2", nativeQuery = true)
    BigDecimal nextNumber(Long quizId, Long questionId);

}
