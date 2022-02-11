package com.test.main.testrest.adapter.controller;

import com.test.main.testrest.adapter.controller.model.CreateQuizRequest;
import com.test.main.testrest.adapter.controller.model.QuizResponse;
import com.test.main.testrest.adapter.controller.model.UpdateQuizRequest;
import com.test.main.testrest.application.port.in.quiz.CreateQuizCommand;
import com.test.main.testrest.application.port.in.quiz.DeleteQuizCommand;
import com.test.main.testrest.application.port.in.quiz.GetAllQuizzesCommand;
import com.test.main.testrest.application.port.in.quiz.GetQuizCommand;
import com.test.main.testrest.application.port.in.quiz.UpdateQuizCommand;
import com.test.main.testrest.domain.Quiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/quizzes")
@Slf4j
public class QuizControllerAdapter {

    private final CreateQuizCommand createQuizCommand;
    private final DeleteQuizCommand deleteQuizCommand;
    private final GetAllQuizzesCommand getAllQuizzesCommand;
    private final GetQuizCommand getQuizCommand;
    private final UpdateQuizCommand updateQuizCommand;

    public QuizControllerAdapter(CreateQuizCommand createQuizCommand,
                                 DeleteQuizCommand deleteQuizCommand,
                                 GetAllQuizzesCommand getAllQuizzesCommand,
                                 GetQuizCommand getQuizCommand,
                                 UpdateQuizCommand updateQuizCommand) {
        this.createQuizCommand = createQuizCommand;
        this.deleteQuizCommand = deleteQuizCommand;
        this.getAllQuizzesCommand = getAllQuizzesCommand;
        this.getQuizCommand = getQuizCommand;
        this.updateQuizCommand = updateQuizCommand;
    }

    @GetMapping()
    private ResponseEntity<List<QuizResponse>> getAllQuiz() {
        log.info("GET /api/quizzes");
        List<Quiz> quizzes = this.getAllQuizzesCommand.execute();
        List<QuizResponse> response = quizzes.stream()
                .map(QuizResponse::fromDomain)
                .collect(Collectors.toList());
        log.info("Response: GET /api/quizzes, {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    private ResponseEntity<QuizResponse> getQuizById(@PathVariable Long id) {
        log.info("GET /api/quizzes/{}", id);
        Quiz quiz = this.getQuizCommand.execute(id);
        QuizResponse response = QuizResponse.fromDomain(quiz);
        log.info("Response: GET /api/quizzes/{}, {}", id, response);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    private ResponseEntity<QuizResponse> createQuiz(@RequestBody CreateQuizRequest request) {
        log.info("POST /api/quizzes, body= {}", request);
        Quiz quiz = this.createQuizCommand.execute(request.toCommand());
        QuizResponse response = QuizResponse.fromDomain(quiz);
        log.info("Response: POST /api/quizzes, {}", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    private ResponseEntity<QuizResponse> updateQuiz(@PathVariable Long id,
                                                    @RequestBody UpdateQuizRequest request) {
        log.info("PUT /api/quizzes/{}, body= {}", id, request);
        Quiz quiz = this.updateQuizCommand.execute(request.toCommand(id));
        QuizResponse response = QuizResponse.fromDomain(quiz);
        log.info("Response: POST /api/quizzes/{}, {}", id, response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteQuiz(@PathVariable Long id) {
        log.info("DELETE /api/quizzes/{}", id);
        this.deleteQuizCommand.execute(id);
        log.info("Response: DELETE /api/quizzes/{}, OK", id);
        return ResponseEntity.ok(null);
    }


}
