package com.test.main.testrest.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.main.testrest.adapter.controller.model.CreateQuizRequest;
import com.test.main.testrest.adapter.controller.model.QuizResponse;
import com.test.main.testrest.adapter.controller.model.UpdateQuizRequest;
import com.test.main.testrest.application.port.in.quiz.CreateQuizCommand;
import com.test.main.testrest.application.port.in.quiz.DeleteQuizCommand;
import com.test.main.testrest.application.port.in.quiz.GetAllQuizzesCommand;
import com.test.main.testrest.application.port.in.quiz.GetQuizCommand;
import com.test.main.testrest.application.port.in.quiz.UpdateQuizCommand;
import com.test.main.testrest.config.TestConfig;
import com.test.main.testrest.config.exception.ErrorCode;
import com.test.main.testrest.config.exception.GenericException;
import com.test.main.testrest.domain.Quiz;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(QuizControllerAdapter.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestConfig.class)
public class QuizControllerAdapterTest {

    private static final Long QUIZ_ID = 1l;
    private static final String GET_QUIZ_URI = "/api/quizzes/1";
    private static final String GET_ALL_QUIZZES_URI = "/api/quizzes";
    private static final String CREATE_QUIZ_URI = "/api/quizzes";
    private static final String UPDATE_QUIZ_URI = "/api/quizzes/1";
    private static final String DELETE_QUIZ_URI = "/api/quizzes/1";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateQuizCommand createQuizCommand;

    @MockBean
    private DeleteQuizCommand deleteQuizCommand;

    @MockBean
    private GetAllQuizzesCommand getAllQuizzesCommand;

    @MockBean
    private GetQuizCommand getQuizCommand;

    @MockBean
    private UpdateQuizCommand updateQuizCommand;

    @Test
    @DisplayName("Cuando obtengo correctamente todos los quiz, entonces regreso un listado")
    public void getAllQuizzesOk() throws Exception {
        List<Quiz> quizzes = getGetAllQuizzesMock();
        String expected = mapper.writeValueAsString(getGetAllQuizzesResponseMock());

        when(getAllQuizzesCommand.execute()).thenReturn(quizzes);

        mockMvc.perform(get(GET_ALL_QUIZZES_URI))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));

        verify(getAllQuizzesCommand, times(1)).execute();
    }

    @Test
    @DisplayName("Cuando hay un error al obtener todos los quizzes, entonces regreso un mensaje de error")
    public void getAllQuizzesError() throws Exception {
        when(getAllQuizzesCommand.execute()).thenThrow(new GenericException(ErrorCode.INTERNAL_ERROR));

        mockMvc.perform(get(GET_ALL_QUIZZES_URI))
                .andExpect(status().is5xxServerError());

        verify(getAllQuizzesCommand, times(1)).execute();
    }

    @Test
    @DisplayName("Cuando obtengo correctamente un quiz, entonces regreso un quiz")
    public void getQuizOk() throws Exception {
        Quiz quiz = getGetQuizMock();
        String expected = mapper.writeValueAsString(getGetQuizResponseMock());

        when(getQuizCommand.execute(eq(QUIZ_ID))).thenReturn(quiz);

        mockMvc.perform(get(GET_QUIZ_URI))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));

        verify(getQuizCommand, times(1)).execute(eq(QUIZ_ID));
    }

    @Test
    @DisplayName("Cuando hay un error al tratar de obtener un quiz, entonces regreso un mensaje de error")
    public void getQuizError() throws Exception {
        when(getQuizCommand.execute(eq(QUIZ_ID))).thenThrow(new GenericException(ErrorCode.INTERNAL_ERROR));

        mockMvc.perform(get(GET_QUIZ_URI))
                .andExpect(status().is5xxServerError());

        verify(getQuizCommand, times(1)).execute(eq(QUIZ_ID));
    }

    @Test
    @DisplayName("Cuando genero correctamente un quiz, entonces regreso el quiz creado")
    public void createQuizOk() throws Exception {
        CreateQuizRequest quizRequest = getCreateQuizRequestMock();
        CreateQuizCommand.QuizCommand command = quizRequest.toCommand();
        Quiz quiz = Quiz.builder()
                .id(1l)
                .title(command.getTitle())
                .description(command.getDescription())
                .build();
        String expected = mapper.writeValueAsString(getCreateQuizResponseMock());

        when(createQuizCommand.execute(eq(command))).thenReturn(quiz);

        mockMvc.perform(post(CREATE_QUIZ_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(quizRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));

        verify(createQuizCommand, times(1)).execute(eq(command));
    }

    @Test
    @DisplayName("Cuando hay un error al tratar de crear un quiz, entonces regreso un mensaje de error")
    public void createQuizError() throws Exception {
        CreateQuizRequest quizRequest = getCreateQuizRequestMock();
        CreateQuizCommand.QuizCommand command = quizRequest.toCommand();

        when(createQuizCommand.execute(eq(command))).thenThrow(new GenericException(ErrorCode.INTERNAL_ERROR));

        mockMvc.perform(post(CREATE_QUIZ_URI).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(quizRequest)))
                .andExpect(status().is5xxServerError());

        verify(createQuizCommand, times(1)).execute(eq(command));
    }

    @Test
    @DisplayName("Cuando actualizo correctamente un quiz, entonces regreso el quiz actualizado")
    public void updateQuizOk() throws Exception {
        UpdateQuizRequest quizRequest = getUpdateQuizRequestMock();
        UpdateQuizCommand.QuizCommand command = quizRequest.toCommand(QUIZ_ID);
        Quiz quiz = Quiz.builder()
                .id(1l)
                .title(command.getTitle())
                .description(command.getDescription())
                .build();
        String expected = mapper.writeValueAsString(getUpdateQuizResponseMock());

        when(updateQuizCommand.execute(eq(command))).thenReturn(quiz);

        mockMvc.perform(put(UPDATE_QUIZ_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(quizRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));

        verify(updateQuizCommand, times(1)).execute(eq(command));
    }

    @Test
    @DisplayName("Cuando hay un error al tratar de actualizar un quiz, entonces regreso un mensaje de error")
    public void updateQuizError() throws Exception {
        UpdateQuizRequest quizRequest = getUpdateQuizRequestMock();
        UpdateQuizCommand.QuizCommand command = quizRequest.toCommand(QUIZ_ID);

        when(updateQuizCommand.execute(eq(command))).thenThrow(new GenericException(ErrorCode.INTERNAL_ERROR));

        mockMvc.perform(put(UPDATE_QUIZ_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(quizRequest)))
                .andExpect(status().is5xxServerError());

        verify(updateQuizCommand, times(1)).execute(eq(command));
    }

    @Test
    @DisplayName("Cuando elimino correctamente un quiz, entonces regreso un mensaje de exito")
    public void deleteQuizOk() throws Exception {
        doNothing().when(deleteQuizCommand).execute(eq(QUIZ_ID));

        mockMvc.perform(delete(DELETE_QUIZ_URI))
                .andExpect(status().isOk());

        verify(deleteQuizCommand, times(1)).execute(eq(QUIZ_ID));
    }

    @Test
    @DisplayName("Cuando hay un error al tratar de eliminar un quiz, entonces regreso un mensaje de error")
    public void deleteQuizError() throws Exception {
        doThrow(new GenericException(ErrorCode.INTERNAL_ERROR)).when(deleteQuizCommand).execute(eq(QUIZ_ID));

        mockMvc.perform(delete(DELETE_QUIZ_URI))
                .andExpect(status().is5xxServerError());

        verify(deleteQuizCommand, times(1)).execute(eq(QUIZ_ID));
    }

    private List<Quiz> getGetAllQuizzesMock() {
        return List.of(
                Quiz.builder()
                        .id(1l)
                        .title("TITLE")
                        .description("DESCRIPTION")
                        .build(),
                Quiz.builder()
                        .id(2l)
                        .title("TITLE")
                        .description("DESCRIPTION")
                        .build(),
                Quiz.builder()
                        .id(3l)
                        .title("TITLE")
                        .description("DESCRIPTION")
                        .build()
        );
    }

    private List<QuizResponse> getGetAllQuizzesResponseMock() {
        return List.of(
                QuizResponse.builder()
                        .id(1l)
                        .title("TITLE")
                        .description("DESCRIPTION")
                        .build(),
                QuizResponse.builder()
                        .id(2l)
                        .title("TITLE")
                        .description("DESCRIPTION")
                        .build(),
                QuizResponse.builder()
                        .id(3l)
                        .title("TITLE")
                        .description("DESCRIPTION")
                        .build()
        );
    }

    private Quiz getGetQuizMock() {
        return Quiz.builder()
                .id(1l)
                .title("TITLE")
                .description("DESCRIPTION")
                .build();
    }

    private QuizResponse getGetQuizResponseMock() {
        return QuizResponse.builder()
                .id(1l)
                .title("TITLE")
                .description("DESCRIPTION")
                .build();
    }

    private CreateQuizRequest getCreateQuizRequestMock() {
        return CreateQuizRequest.builder()
                .title("TITLE")
                .description("DESCRIPTION")
                .build();
    }

    private UpdateQuizRequest getUpdateQuizRequestMock() {
        return UpdateQuizRequest.builder()
                .title("TITLE")
                .description("DESCRIPTION")
                .build();
    }

    private QuizResponse getCreateQuizResponseMock() {
        return QuizResponse.builder()
                .id(1l)
                .title("TITLE")
                .description("DESCRIPTION")
                .build();
    }

    private QuizResponse getUpdateQuizResponseMock() {
        return QuizResponse.builder()
                .id(1l)
                .title("TITLE")
                .description("DESCRIPTION")
                .build();
    }

}
