package com.test.main.testrest.adapter.controller.model;

import com.test.main.testrest.application.port.in.option.CreateOptionCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOptionRequest {

    @NotBlank(message = "The [description] can not be null or empty")
    @Size(max = 225, message = "The [description] can not be exceeded of 225 characters")
    private String description;

    public CreateOptionCommand.OptionCommand toCommand(Long quizId, Long questionId) {
        return CreateOptionCommand.OptionCommand.builder()
                .quizId(quizId)
                .questionId(questionId)
                .description(description)
                .build();
    }

}
