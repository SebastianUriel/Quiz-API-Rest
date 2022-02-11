package com.test.main.testrest.adapter.controller;

import com.test.main.testrest.adapter.controller.model.CreateQuestionRequest;
import com.test.main.testrest.adapter.controller.model.QuestionResponse;
import com.test.main.testrest.adapter.controller.model.UpdateQuestionRequest;
import com.test.main.testrest.application.port.in.question.CreateQuestionCommand;
import com.test.main.testrest.application.port.in.question.DeleteQuestionCommand;
import com.test.main.testrest.application.port.in.question.GetQuestionCommand;
import com.test.main.testrest.application.port.in.question.UpdateQuestionCommand;
import com.test.main.testrest.domain.Question;
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
@RequestMapping("/api/quizzes/{quizId}/questions")
@Slf4j
public class QuestionControllerAdapter {

    private final CreateQuestionCommand createQuestionCommand;
    private final DeleteQuestionCommand deleteQuestionCommand;
    private final GetQuestionCommand getQuestionCommand;
    private final UpdateQuestionCommand updateQuestionCommand;

    public QuestionControllerAdapter(CreateQuestionCommand createQuestionCommand,
                                     DeleteQuestionCommand deleteQuestionCommand,
                                     GetQuestionCommand getQuestionCommand,
                                     UpdateQuestionCommand updateQuestionCommand) {
        this.createQuestionCommand = createQuestionCommand;
        this.deleteQuestionCommand = deleteQuestionCommand;
        this.getQuestionCommand = getQuestionCommand;
        this.updateQuestionCommand = updateQuestionCommand;
    }

    @GetMapping()
    private ResponseEntity<List<QuestionResponse>> getQuestionById(@PathVariable Long quizId) {
        log.info("GET /api/quizzes/{}/questions", quizId);
        List<Question> questions = this.getQuestionCommand.execute(quizId);
        List<QuestionResponse> response = questions.stream()
                .map(QuestionResponse::fromDomain)
                .collect(Collectors.toList());
        log.info("Response: GET /api/quizzes/{}/questions, {}", quizId, response);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    private ResponseEntity<QuestionResponse> createQuestion(@PathVariable Long quizId,
                                                            @RequestBody CreateQuestionRequest request) {
        log.info("POST /api/quizzes/{}/questions, body= {}", quizId, request);
        Question question = this.createQuestionCommand.execute(request.toCommand(quizId));
        QuestionResponse response = QuestionResponse.fromDomain(question);
        log.info("Response: POST /api/quizzes/{}/questions, {}", quizId, response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    private ResponseEntity<QuestionResponse> updateQuestion(@PathVariable Long quizId,
                                                            @PathVariable Long id,
                                                            @RequestBody UpdateQuestionRequest request) {
        log.info("PUT /api/quizzes/{}/questions/{}, body= {}", quizId, id, request);
        Question question = this.updateQuestionCommand.execute(request.toCommand(quizId, id));
        QuestionResponse response = QuestionResponse.fromDomain(question);
        log.info("Response: PUT /api/quizzes/{}/questions/{}, {}", quizId, id, response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteQuestion(@PathVariable Long quizId,
                                             @PathVariable Long id) {
        log.info("DELETE /api/quizzes/{}/questions/{}", quizId, id);
        this.deleteQuestionCommand.execute(quizId, id);
        log.info("Response: DELETE /api/quizzes/{}/questions/{}, OK", quizId, id);
        return ResponseEntity.ok(null);
    }

}
