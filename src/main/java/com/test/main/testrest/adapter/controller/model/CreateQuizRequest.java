package com.test.main.testrest.adapter.controller.model;

import com.test.main.testrest.application.port.in.quiz.CreateQuizCommand;
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
public class CreateQuizRequest {

    @NotBlank(message = "The [title] can not be null or empty")
    @Size(max = 25, message = "The [title] can not be exceeded of 25 characters")
    private String title;

    @NotBlank(message = "The [description] can not be null or empty")
    @Size(max = 225, message = "The [description] can not be exceeded of 225 characters")
    private String description;
    
    public CreateQuizCommand.QuizCommand toCommand() {
        return CreateQuizCommand.QuizCommand.builder()
                .title(title)
                .description(description)
                .build();
    }

}
