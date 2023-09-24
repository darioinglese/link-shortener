package com.farmu.challenge.adapter.controller;

import com.farmu.challenge.adapter.controller.model.ImageRequest;
import com.farmu.challenge.adapter.controller.model.ImageResponse;
import com.farmu.challenge.application.ResizeUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Tests for Image Controller")
@WebMvcTest(ImageController.class)
@Import(ImageController.class)
class ImageControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ResizeUseCase resizeUseCase;

    private final static String base64big = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAAd0lEQVR4XmNggIIXPrb/YRgmhgFeu9tKIivEqeHW7Vv/FRQU4ApA7E+fPmEqBEnCwBcojdXEZ8+eQSRzEsEYBEBiIAxXBNIJE4RpwFCETSEyxlAIogP9Av6DsI+3L5gGiYHYMIyiCQZgElB/gQG6GjCwsDbDkAAA3HWh/oxNDGYAAAAASUVORK5CYII=";
    private final static String base64small = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAQAAAAECAYAAACp8Z5+AAAAJklEQVR4XmN47W4r+cLH9j8IM4AAiAECcIFnz579h2GwADIAqQQAS/4eVK/1kz8AAAAASUVORK5CYII=";


    @Test
    void resizeOk() throws Exception {
        var request = buildRequest();
        when(resizeUseCase.execute(request)).thenReturn(base64small);
        var expected = new ImageResponse(base64small);
        mockMvc.perform(
                        post("/resize")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())


                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(expected)));
    }

    private ImageRequest buildRequest() {
        return new ImageRequest(base64big,4,4);
    }
}