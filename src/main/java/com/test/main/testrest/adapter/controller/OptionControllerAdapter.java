package com.test.main.testrest.adapter.controller;

import com.test.main.testrest.adapter.controller.model.CreateOptionRequest;
import com.test.main.testrest.adapter.controller.model.OptionResponse;
import com.test.main.testrest.adapter.controller.model.UpdateOptionRequest;
import com.test.main.testrest.application.port.in.option.CreateOptionCommand;
import com.test.main.testrest.application.port.in.option.DeleteOptionCommand;
import com.test.main.testrest.application.port.in.option.GetOptionCommand;
import com.test.main.testrest.application.port.in.option.UpdateOptionCommand;
import com.test.main.testrest.domain.Option;
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
@RequestMapping("/api/quizzes/{quizId}/questions/{questionId}/options")
@Slf4j
public class OptionControllerAdapter {

    private final CreateOptionCommand createOptionCommand;
    private final DeleteOptionCommand deleteOptionCommand;
    private final GetOptionCommand getOptionCommand;
    private final UpdateOptionCommand updateOptionCommand;

    public OptionControllerAdapter(CreateOptionCommand createOptionCommand,
                                   DeleteOptionCommand deleteOptionCommand,
                                   GetOptionCommand getOptionCommand,
                                   UpdateOptionCommand updateOptionCommand) {
        this.createOptionCommand = createOptionCommand;
        this.deleteOptionCommand = deleteOptionCommand;
        this.getOptionCommand = getOptionCommand;
        this.updateOptionCommand = updateOptionCommand;
    }

    @GetMapping()
    private ResponseEntity<List<OptionResponse>> getOptionById(@PathVariable Long quizId,
                                                         @PathVariable Long questionId) {
        log.info("GET /api/quizzes/{}/questions/{}/options", quizId, questionId);
        List<Option> options = this.getOptionCommand.execute(quizId, questionId);
        List<OptionResponse> response = options.stream()
                .map(OptionResponse::fromDomain)
                .collect(Collectors.toList());
        log.info("Response: GET /api/quizzes/{}/questions/{}/options, {}", quizId, questionId, response);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    private ResponseEntity<OptionResponse> createOption(@PathVariable Long quizId,
                                                        @PathVariable Long questionId,
                                                        @RequestBody CreateOptionRequest request) {
        log.info("POST /api/quizzes/{}/questions/{}/options, body= {}", quizId, questionId, request);
        Option option = this.createOptionCommand.execute(request.toCommand(quizId, questionId));
        OptionResponse response = OptionResponse.fromDomain(option);
        log.info("Response: POST /api/quizzes/{}/questions/{}/options, {}", quizId, questionId, response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    private ResponseEntity<OptionResponse> updateOption(@PathVariable Long quizId,
                                                        @PathVariable Long questionId,
                                                        @PathVariable Long id,
                                                        @RequestBody UpdateOptionRequest request) {
        log.info("PUT /api/quizzes/{}/questions/{}/options/{}, body= {}", quizId, questionId, id, request);
        Option option = this.updateOptionCommand.execute(request.toCommand(quizId, questionId, id));
        OptionResponse response = OptionResponse.fromDomain(option);
        log.info("Response: PUT /api/quizzes/{}/questions/{}/options/{}, {}", quizId, questionId, id, response);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteOption(@PathVariable Long quizId,
                                           @PathVariable Long questionId,
                                           @PathVariable Long id) {
        log.info("DELETE /api/quizzes/{}/questions/{}/options/{}", quizId, questionId, id);
        this.deleteOptionCommand.execute(quizId, questionId, id);
        log.info("Response: DELETE /api/quizzes/{}/questions/{}/options/{}, OK", quizId, questionId, id);
        return ResponseEntity.ok(null);
    }

}
