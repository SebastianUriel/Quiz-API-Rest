package com.test.main.testrest.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.main.testrest.adapter.controller.model.CreateOptionRequest;
import com.test.main.testrest.adapter.controller.model.OptionResponse;
import com.test.main.testrest.adapter.controller.model.UpdateOptionRequest;
import com.test.main.testrest.application.port.in.option.CreateOptionCommand;
import com.test.main.testrest.application.port.in.option.DeleteOptionCommand;
import com.test.main.testrest.application.port.in.option.GetOptionCommand;
import com.test.main.testrest.application.port.in.option.UpdateOptionCommand;
import com.test.main.testrest.config.TestConfig;
import com.test.main.testrest.config.exception.ErrorCode;
import com.test.main.testrest.config.exception.GenericException;
import com.test.main.testrest.domain.Option;
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

@WebMvcTest(OptionControllerAdapter.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestConfig.class)
public class OptionControllerAdapterTest {

    private static final Long QUIZ_ID = 1l;
    private static final Long QUESTION_ID = 1l;
    private static final Long NUMBER = 1l;
    private static final String GET_OPTION_URI = "/api/quizzes/1/questions/1/options";
    private static final String CREATE_OPTION_URI = "/api/quizzes/1/questions/1/options";
    private static final String UPDATE_OPTION_URI = "/api/quizzes/1/questions/1/options/1";
    private static final String DELETE_OPTION_URI = "/api/quizzes/1/questions/1/options/1";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateOptionCommand createOptionCommand;

    @MockBean
    private DeleteOptionCommand deleteOptionCommand;

    @MockBean
    private GetOptionCommand getOptionCommand;

    @MockBean
    private UpdateOptionCommand updateOptionCommand;

    @Test
    @DisplayName("Cuando obtengo correctamente un Option, entonces regreso un Option")
    public void getOptionOk() throws Exception {
        List<Option> question = getGetOptionMock();
        String expected = mapper.writeValueAsString(getGetOptionResponseMock());

        when(getOptionCommand.execute(eq(QUIZ_ID), eq(QUESTION_ID))).thenReturn(question);

        mockMvc.perform(get(GET_OPTION_URI))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));

        verify(getOptionCommand, times(1)).execute(eq(QUIZ_ID), eq(QUESTION_ID));
    }

    @Test
    @DisplayName("Cuando hay un error al tratar de obtener un Option, entonces regreso un mensaje de error")
    public void getOptionError() throws Exception {
        when(getOptionCommand.execute(eq(QUIZ_ID), eq(QUESTION_ID))).thenThrow(new GenericException(ErrorCode.INTERNAL_ERROR));

        mockMvc.perform(get(GET_OPTION_URI))
                .andExpect(status().is5xxServerError());

        verify(getOptionCommand, times(1)).execute(eq(QUIZ_ID), eq(QUESTION_ID));
    }

    @Test
    @DisplayName("Cuando genero correctamente un Option, entonces regreso el Option creado")
    public void createOptionOk() throws Exception {
        CreateOptionRequest questionRequest = getCreateOptionRequestMock();
        CreateOptionCommand.OptionCommand command = questionRequest.toCommand(QUIZ_ID, QUESTION_ID);
        Option question = Option.builder()
                .quizId(command.getQuizId())
                .number(NUMBER)
                .questionId(command.getQuestionId())
                .description(command.getDescription())
                .build();
        String expected = mapper.writeValueAsString(getCreateOptionResponseMock());

        when(createOptionCommand.execute(eq(command))).thenReturn(question);

        mockMvc.perform(post(CREATE_OPTION_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(questionRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));

        verify(createOptionCommand, times(1)).execute(eq(command));
    }

    @Test
    @DisplayName("Cuando hay un error al tratar de crear un Option, entonces regreso un mensaje de error")
    public void createOptionError() throws Exception {
        CreateOptionRequest questionRequest = getCreateOptionRequestMock();
        CreateOptionCommand.OptionCommand command = questionRequest.toCommand(QUIZ_ID, QUESTION_ID);

        when(createOptionCommand.execute(eq(command))).thenThrow(new GenericException(ErrorCode.INTERNAL_ERROR));

        mockMvc.perform(post(CREATE_OPTION_URI).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(questionRequest)))
                .andExpect(status().is5xxServerError());

        verify(createOptionCommand, times(1)).execute(eq(command));
    }

    @Test
    @DisplayName("Cuando actualizo correctamente un Option, entonces regreso el Option actualizado")
    public void updateOptionOk() throws Exception {
        UpdateOptionRequest questionRequest = getUpdateOptionRequestMock();
        UpdateOptionCommand.OptionCommand command = questionRequest.toCommand(QUIZ_ID, QUESTION_ID, NUMBER);
        Option question = Option.builder()
                .quizId(command.getQuizId())
                .questionId(command.getQuestionId())
                .number(command.getNumber())
                .description(command.getDescription())
                .build();
        String expected = mapper.writeValueAsString(getUpdateOptionResponseMock());

        when(updateOptionCommand.execute(eq(command))).thenReturn(question);

        mockMvc.perform(put(UPDATE_OPTION_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(questionRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(expected));

        verify(updateOptionCommand, times(1)).execute(eq(command));
    }
    @Test
    @DisplayName("Cuando hay un error al tratar de actualizar un Option, entonces regreso un mensaje de error")
    public void updateOptionError() throws Exception {
        UpdateOptionRequest questionRequest = getUpdateOptionRequestMock();
        UpdateOptionCommand.OptionCommand command = questionRequest.toCommand(QUIZ_ID, QUESTION_ID, NUMBER);

        when(updateOptionCommand.execute(eq(command))).thenThrow(new GenericException(ErrorCode.INTERNAL_ERROR));

        mockMvc.perform(put(UPDATE_OPTION_URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(questionRequest)))
                .andExpect(status().is5xxServerError());

        verify(updateOptionCommand, times(1)).execute(eq(command));
    }


    @Test
    @DisplayName("Cuando elimino correctamente un Option, entonces regreso un mensaje de exito")
    public void deleteOptionOk() throws Exception {
        doNothing().when(deleteOptionCommand).execute(eq(QUIZ_ID), eq(QUESTION_ID), eq(NUMBER));

        mockMvc.perform(delete(DELETE_OPTION_URI))
                .andExpect(status().isOk());

        verify(deleteOptionCommand, times(1)).execute(eq(QUIZ_ID), eq(QUESTION_ID), eq(NUMBER));
    }

    @Test
    @DisplayName("Cuando hay un error al tratar de eliminar un Option, entonces regreso un mensaje de error")
    public void deleteOptionError() throws Exception {
        doThrow(new GenericException(ErrorCode.INTERNAL_ERROR)).when(deleteOptionCommand).execute(eq(QUIZ_ID), eq(QUESTION_ID), eq(NUMBER));

        mockMvc.perform(delete(DELETE_OPTION_URI))
                .andExpect(status().is5xxServerError());

        verify(deleteOptionCommand, times(1)).execute(eq(QUIZ_ID), eq(QUESTION_ID), eq(NUMBER));
    }

    private List<Option> getGetOptionMock() {
        return List.of(Option.builder()
                .quizId(1l)
                .questionId(1l)
                .number(1l)
                .description("DESCRIPTION")
                .build());
    }

    private List<OptionResponse> getGetOptionResponseMock() {
        return List.of(OptionResponse.builder()
                .quizId(1l)
                .questionId(1l)
                .number(1l)
                .description("DESCRIPTION")
                .build());
    }

    private CreateOptionRequest getCreateOptionRequestMock() {
        return CreateOptionRequest.builder()
                .description("DESCRIPTION")
                .build();
    }

    private UpdateOptionRequest getUpdateOptionRequestMock() {
        return UpdateOptionRequest.builder()
                .description("DESCRIPTION")
                .build();
    }

    private OptionResponse getCreateOptionResponseMock() {
        return OptionResponse.builder()
                .quizId(1l)
                .questionId(1l)
                .number(1l)
                .description("DESCRIPTION")
                .build();
    }

    private OptionResponse getUpdateOptionResponseMock() {
        return OptionResponse.builder()
                .quizId(1l)
                .questionId(1l)
                .number(1l)
                .description("DESCRIPTION")
                .build();
    }

}
