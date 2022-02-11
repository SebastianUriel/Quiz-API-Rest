package com.test.main.testrest.application.usecase.quiz;

import com.test.main.testrest.application.port.in.quiz.GetAllQuizzesCommand;
import com.test.main.testrest.application.port.out.OptionJpaRepository;
import com.test.main.testrest.application.port.out.QuestionJpaRepository;
import com.test.main.testrest.application.port.out.QuizJpaRepository;
import com.test.main.testrest.domain.Option;
import com.test.main.testrest.domain.Question;
import com.test.main.testrest.domain.Quiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class GetAllQuizzesUseCase implements GetAllQuizzesCommand {

    public final QuizJpaRepository quizJpaRepository;
    public final QuestionJpaRepository questionJpaRepository;
    public final OptionJpaRepository optionJpaRepository;

    public GetAllQuizzesUseCase(QuizJpaRepository quizJpaRepository,
                                QuestionJpaRepository questionJpaRepository,
                                OptionJpaRepository optionJpaRepository) {
        this.quizJpaRepository = quizJpaRepository;
        this.questionJpaRepository = questionJpaRepository;
        this.optionJpaRepository = optionJpaRepository;
    }

    @Override
    public List<Quiz> execute() {
        log.info("Start - GetAllQuizzesUseCase");
        List<Quiz> quizzes = this.quizJpaRepository.getAll();

        quizzes = Optional.ofNullable(quizzes)
                .map(values -> values.stream()
                        .map(this.getQuestions())
                        .collect(Collectors.toList()))
                .orElse(new ArrayList());

        log.info("End - GetAllQuizzesUseCase, result: {}", quizzes);
        return quizzes;
    }

    private Function<Quiz, Quiz> getQuestions() {
        return value -> {
            List<Question> questions = this.questionJpaRepository.get(value.getId());
            questions = Optional.ofNullable(questions)
                    .map(subValues -> subValues.stream()
                            .map(this.getOptions())
                            .collect(Collectors.toList()))
                    .orElse(new ArrayList<>());
            return value.withQuestions(questions);
        };
    }

    private Function<Question, Question> getOptions() {
        return subValue -> {
            List<Option> options = this.optionJpaRepository.get(subValue.getQuizId(), subValue.getNumber());
            return subValue.withOptions(Optional.ofNullable(options).orElse(new ArrayList()));
        };
    }

}
