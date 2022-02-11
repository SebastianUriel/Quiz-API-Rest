package com.test.main.testrest.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.main.testrest.adapter.controller.model.CreateQuestionRequest;
import com.test.main.testrest.adapter.controller.model.QuestionResponse;
import com.test.main.testrest.adapter.controller.model.UpdateQuestionRequest;
import com.test.main.testrest.application.port.in.question.CreateQuestionCommand;
import com.test.main.testrest.application.port.in.question.DeleteQuestionCommand;
import com.test.main.testrest.application.port.in.question.GetQuestionCommand;
import com.test.main.testrest.application.port.in.question.UpdateQuestionCommand;
import com.test.main.testrest.config.TestConfig;
import com.test.main.testrest.config.exception.ErrorCode;
import com.test.main.testrest.config.exception.GenericException;
import com.test.main.testrest.domain.Question;
import com.test.main.testrest.domain.TypeOption;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuestionControllerAdapter.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestConfig.class)
public class QuestionControllerAdapterTest {

    private static final Long QUIZ_ID = 1l;
    private static final Long NUMBER = 1l;
    private static final String GET_QUESTION_URI = "/api/quizzes/1/questions";
    private static final String CREATE_QUESTION_URI = "/api/quizzes/1/questions";
    private static final String UPDATE_QUESTION_URI = "/api/quizzes/1/questions/1";
    private static final String DELETE_QUESTION_URI = "/api/quizzes/1/questions/1";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateQuestionCommand createQuestionCommand;

    @MockBean
    private DeleteQuestionCommand deleteQuestionCommand;

    @MockBean
    private GetQuestionCommand getQuestionCommand;

    @MockBean
    private UpdateQuestionCommand updateQuestionCommand;

    @Test
    @DisplayName("Cuando obtengo correctamente un Question, entonces regreso un Question")
    public void getQuestionOk() throws Exception {
        List<Question> question = getGetQuestionMock();
        String expected = mapper.writeValueAsString(getGetQuestionResponseMock());

        when(getQuestionCommand.execute(eq(QUIZ_ID))).thenReturn(question);

        mockMvc.perform(get(GET_QUESTION_URI))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));

        verify(getQuestionCommand, times(1)).execute(eq(QUIZ_ID));
    }

    @Test
    @DisplayName("Cuando hay un error al tratar de obtener un Question, entonces regreso un mensaje de error")
    public void getQuestionError() throws Exception {
        when(getQuestionCommand.execute(eq(QUIZ_ID))).thenThrow(new GenericException(ErrorCode.INTERNAL_ERROR));

        mockMvc.perform(get(GET_QUESTION_URI))
                .andExpect(status().is5xxServerError());

        verify(getQuestionCommand, times(1)).execute(eq(QUIZ_ID));
    }

    @Test
    @DisplayName("Cuando genero correctamente un Question, entonces regreso el Question creado")
    public void createQuestionOk() throws Exception {
        CreateQuestionRequest questionRequest = getCreateQuestionRequestMock();
        CreateQuestionCommand.QuestionCommand command = questionRequest.toCommand(QUIZ_ID);
        Question question = Question.builder()
                .quizId(command.getQuizId())
                .number(NUMBER)
                .type(command.getType())
                .description(command.getDescription())
                .build();
        String expected = mapper.writeValueAsString(getCreateQuestionResponseMock());

        when(createQuestionCommand.execute(eq(command))).thenReturn(question);

        mockMvc.perform(post(CREATE_QUESTION_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(questionRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));

        verify(createQuestionCommand, times(1)).execute(eq(command));
    }

    @Test
    @DisplayName("Cuando hay un error al tratar de crear un Question, entonces regreso un mensaje de error")
    public void createQuestionError() throws Exception {
        CreateQuestionRequest questionRequest = getCreateQuestionRequestMock();
        CreateQuestionCommand.QuestionCommand command = questionRequest.toCommand(QUIZ_ID);

        when(createQuestionCommand.execute(eq(command))).thenThrow(new GenericException(ErrorCode.INTERNAL_ERROR));

        mockMvc.perform(post(CREATE_QUESTION_URI).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(questionRequest)))
                .andExpect(status().is5xxServerError());

        verify(createQuestionCommand, times(1)).execute(eq(command));
    }

    @Test
    @DisplayName("Cuando actualizo correctamente un Question, entonces regreso el Question actualizado")
    public void updateQuestionOk() throws Exception {
        UpdateQuestionRequest questionRequest = getUpdateQuestionRequestMock();
        UpdateQuestionCommand.QuestionCommand command = questionRequest.toCommand(QUIZ_ID, NUMBER);
        Question question = Question.builder()
                .quizId(command.getQuizId())
                .number(command.getNumber())
                .type(command.getType())
                .description(command.getDescription())
                .build();
        String expected = mapper.writeValueAsString(getUpdateQuestionResponseMock());

        when(updateQuestionCommand.execute(eq(command))).thenReturn(question);

        mockMvc.perform(put(UPDATE_QUESTION_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(questionRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));

        verify(updateQuestionCommand, times(1)).execute(eq(command));
    }

    @Test
    @DisplayName("Cuando hay un error al tratar de actualizar un Question, entonces regreso un mensaje de error")
    public void updateQuestionError() throws Exception {
        UpdateQuestionRequest questionRequest = getUpdateQuestionRequestMock();
        UpdateQuestionCommand.QuestionCommand command = questionRequest.toCommand(QUIZ_ID, NUMBER);

        when(updateQuestionCommand.execute(eq(command))).thenThrow(new GenericException(ErrorCode.INTERNAL_ERROR));

        mockMvc.perform(put(UPDATE_QUESTION_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(questionRequest)))
                .andExpect(status().is5xxServerError());

        verify(updateQuestionCommand, times(1)).execute(eq(command));
    }

    @Test
    @DisplayName("Cuando elimino correctamente un Question, entonces regreso un mensaje de exito")
    public void deleteQuestionOk() throws Exception {
        doNothing().when(deleteQuestionCommand).execute(eq(QUIZ_ID), eq(NUMBER));

        mockMvc.perform(delete(DELETE_QUESTION_URI))
                .andExpect(status().isOk());

        verify(deleteQuestionCommand, times(1)).execute(eq(QUIZ_ID), eq(NUMBER));
    }

    @Test
    @DisplayName("Cuando hay un error al tratar de eliminar un Question, entonces regreso un mensaje de error")
    public void deleteQuestionError() throws Exception {
        doThrow(new GenericException(ErrorCode.INTERNAL_ERROR)).when(deleteQuestionCommand).execute(eq(QUIZ_ID), eq(NUMBER));

        mockMvc.perform(delete(DELETE_QUESTION_URI))
                .andExpect(status().is5xxServerError());

        verify(deleteQuestionCommand, times(1)).execute(eq(QUIZ_ID), eq(NUMBER));
    }

    private List<Question> getGetQuestionMock() {
        return List.of(Question.builder()
                .quizId(1l)
                .number(1l)
                .description("DESCRIPTION")
                .type(TypeOption.OPEN)
                .build());
    }

    private List<QuestionResponse> getGetQuestionResponseMock() {
        return List.of(QuestionResponse.builder()
                .quizId(1l)
                .number(1l)
                .description("DESCRIPTION")
                .type(TypeOption.OPEN.toString())
                .build());
    }

    private CreateQuestionRequest getCreateQuestionRequestMock() {
        return CreateQuestionRequest.builder()
                .description("DESCRIPTION")
                .type(TypeOption.OPEN)
                .build();
    }

    private UpdateQuestionRequest getUpdateQuestionRequestMock() {
        return UpdateQuestionRequest.builder()
                .description("DESCRIPTION")
                .type(TypeOption.OPEN)
                .build();
    }

    private QuestionResponse getCreateQuestionResponseMock() {
        return QuestionResponse.builder()
                .quizId(1l)
                .number(1l)
                .description("DESCRIPTION")
                .type(TypeOption.OPEN.toString())
                .build();
    }

    private QuestionResponse getUpdateQuestionResponseMock() {
        return QuestionResponse.builder()
                .quizId(1l)
                .number(1l)
                .description("DESCRIPTION")
                .type(TypeOption.OPEN.toString())
                .build();
    }

}
