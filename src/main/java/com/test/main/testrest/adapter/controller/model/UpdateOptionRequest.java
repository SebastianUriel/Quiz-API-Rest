package com.test.main.testrest.adapter.controller.model;

import com.test.main.testrest.application.port.in.option.UpdateOptionCommand;
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
public class UpdateOptionRequest {

    @NotBlank(message = "The [description] can not be null or empty")
    @Size(max = 225, message = "The [description] can not be exceeded of 225 characters")
    private String description;

    public UpdateOptionCommand.OptionCommand toCommand(Long quizId, Long questionId, Long number) {
        return UpdateOptionCommand.OptionCommand.builder()
                .quizId(quizId)
                .questionId(questionId)
                .number(number)
                .description(description)
                .build();
    }

}
