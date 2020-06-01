package com.epam.brest.courses.rest_app;

import com.epam.brest.courses.model.dto.RentDto;
import com.epam.brest.courses.rest_app.controller.RentDtoRestController;
import com.epam.brest.courses.rest_app.exception_handler.ExceptionRestControllerAdviser;
import com.epam.brest.courses.service_api.dto.RentDtoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RentDtoRestControllerMockTest {

    private MockMvc mockMvc;

    private static final String RENT_DTOS_ENDPOINT = "/rent_dtos";
    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Mock
    private RentDtoService rentDtoService;

    @InjectMocks
    private RentDtoRestController rentDtoRestController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(rentDtoRestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(new ExceptionRestControllerAdviser())
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(rentDtoService);
    }

    @Test
    void shouldFindAllWIthDressNameByDate() throws Exception {
        RentDto firstRentDto = new RentDto();
        firstRentDto.setRentId(1);
        firstRentDto.setClient("First client");
        firstRentDto.setRentDate(LocalDate.of(2020, 1, 1));
        firstRentDto.setDressName("Dress 1");
        RentDto secondRent = new RentDto();
        secondRent.setRentId(2);
        secondRent.setClient("Second client");
        secondRent.setRentDate(LocalDate.of(2021, 1, 1));
        secondRent.setDressName("Dress 2");
        List<RentDto> rentDtos = Arrays.asList(firstRentDto, secondRent);

        when(rentDtoService.findAllWIthDressNameByDate(any(), any())).thenReturn(rentDtos);

        mockMvc.perform(get(RENT_DTOS_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(rentDtos)));

        verify(rentDtoService, times(1)).findAllWIthDressNameByDate(any(), any());
    }

    @Test
    void shouldReturnUnprocessableEntityWhenFindAllWIthDressNameAndDateFromIsAfterDateTo() throws Exception {
        when(rentDtoService.findAllWIthDressNameByDate(any(), any())).thenThrow(new IllegalArgumentException());

        mockMvc.perform(get(RENT_DTOS_ENDPOINT))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(rentDtoService, times(1)).findAllWIthDressNameByDate(any(), any());
    }
}