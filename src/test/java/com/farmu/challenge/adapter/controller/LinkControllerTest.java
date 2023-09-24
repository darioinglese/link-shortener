package com.farmu.challenge.adapter.controller;

import com.farmu.challenge.adapter.controller.model.LinkRequest;
import com.farmu.challenge.application.ShortenLinkUseCase;
import com.farmu.challenge.application.UnshortenLinkUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Tests for Link Controller")
@WebMvcTest(LinkController.class)
@Import(LinkController.class)
class LinkControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ShortenLinkUseCase shortenLinkUseCase;
    @MockBean
    private UnshortenLinkUseCase unshortenLinkUseCase;

    @Test
    void shortenLinkOk() throws Exception {
        String url = "http://www.test.com";
        String expected = "sUrl";
        LinkRequest request = new LinkRequest(url);
        when(shortenLinkUseCase.execute(anyString(), eq(request)))
                .thenReturn(expected);

        mockMvc.perform(
                        post("/shorten")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())


                .andExpect(status().isOk())
                .andExpect(content().string(expected));
    }

    @Test
    void unshortenLinkOk() throws Exception {
        String request = "sUrl";
        String expected = "http://www.test.com";
        when(unshortenLinkUseCase.execute(request))
                .thenReturn(expected);

        mockMvc.perform(
                        get("/{request}", request)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())


                .andExpect(status().isMovedPermanently())
                .andExpect(MockMvcResultMatchers.redirectedUrl(expected));
    }
}