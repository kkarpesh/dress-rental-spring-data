package com.epam.brest.courses.web_app.exception_handler;

import com.epam.brest.courses.service_api.DressService;
import com.epam.brest.courses.web_app.controller.DressController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@ExtendWith(MockitoExtension.class)
class ExceptionControllerAdviserTest {

    private MockMvc mockMvc;
    private static final String DRESSES_ENDPOINT = "/dresses";

    @Mock
    private DressService dressService;

    @InjectMocks
    private DressController dressController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dressController)
                .setControllerAdvice(new ExceptionControllerAdviser())
                .build();

    }

    @AfterEach
    void after() {
        Mockito.reset(dressService);
    }

    @Test
    void handleClientError() throws Exception {
        Mockito.when(dressService.findById(1)).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get(DRESSES_ENDPOINT + "/" + 1)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("message"));

    }

    @Test
    void handleServerError() throws Exception {
        Mockito.when(dressService.findById(1)).thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        mockMvc.perform(get(DRESSES_ENDPOINT + "/" + 1)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("message"));

    }

    @Test
    void handleAccessError() throws Exception {
        Mockito.when(dressService.findById(1)).thenThrow(new ResourceAccessException("Error"));

        mockMvc.perform(get(DRESSES_ENDPOINT + "/" + 1)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("message"));

    }
}