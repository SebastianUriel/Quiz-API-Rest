package com.test.main.testrest.application.usecase.quiz;

import com.test.main.testrest.application.port.in.quiz.GetQuizCommand;
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
public class GetQuizUseCase implements GetQuizCommand {

    public final QuizJpaRepository quizJpaRepository;
    public final QuestionJpaRepository questionJpaRepository;
    public final OptionJpaRepository optionJpaRepository;

    public GetQuizUseCase(QuizJpaRepository quizJpaRepository,
                          QuestionJpaRepository questionJpaRepository,
                          OptionJpaRepository optionJpaRepository) {
        this.quizJpaRepository = quizJpaRepository;
        this.questionJpaRepository = questionJpaRepository;
        this.optionJpaRepository = optionJpaRepository;
    }

    @Override
    public Quiz execute(Long id) {
        log.info("Start - GetQuizUseCase, id: {}", id);
        Quiz quiz = this.quizJpaRepository.get(id);

        List<Question> questions = this.questionJpaRepository.get(id);
        questions = Optional.ofNullable(questions)
                .map(values -> values.stream()
                        .map(this.getOptions())
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());
        quiz = quiz.withQuestions(questions);

        log.info("End - GetQuizUseCase, result: {}", quiz);
        return quiz;
    }

    private Function<Question, Question> getOptions() {
        return subValue -> {
            List<Option> options = this.optionJpaRepository.get(subValue.getQuizId(), subValue.getNumber());
            return subValue.withOptions(Optional.ofNullable(options).orElse(new ArrayList()));
        };
    }

}
