package com.test.main.testrest.adapter.jpa.repository;

import com.test.main.testrest.adapter.jpa.entity.QuizEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends CrudRepository<QuizEntity, Long> {

    List<QuizEntity> findByAvailable(Boolean available);
    List<QuizEntity> findByIdAndAvailable(Long id, Boolean available);

}
