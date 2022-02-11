package com.test.main.testrest.adapter.jpa.entity;

import com.test.main.testrest.domain.Quiz;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity()
@Table(name = "quiz")
public class QuizEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", columnDefinition = "varchar(25) not null")
    private String title;

    @Column(name = "description", columnDefinition = "varchar(225) not null")
    private String description;

    @Column(name = "create_at", columnDefinition = "datetime not null default now()")
    private Date createAt;

    @Column(name = "last_update", columnDefinition = "datetime not null default now()")
    private Date lastUpdate;

    @Column(name = "available", columnDefinition = "boolean not null default 1")
    private Boolean available;

    public Quiz toDomain() {
        return Quiz.builder()
                .id(id)
                .title(title)
                .description(description)
                .build();
    }

    public static QuizEntity fromDomain(Quiz quiz) {
        return QuizEntity.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .description(quiz.getDescription())
                .createAt(new Date())
                .lastUpdate(new Date())
                .available(true)
                .build();
    }

}
