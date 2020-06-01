package com.epam.brest.courses.rest_app;

import com.epam.brest.courses.model.dto.DressDto;
import com.epam.brest.courses.rest_app.controller.DressDtoRestController;
import com.epam.brest.courses.service_api.dto.DressDtoService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class DressDtoRestControllerMockTest {

    private MockMvc mockMvc;

    private static final String DRESS_DTOS_ENDPOINT = "/dress_dtos";
    ObjectMapper mapper = new ObjectMapper();

    @Mock
    private DressDtoService dressDtoService;

    @InjectMocks
    private DressDtoRestController dressDtoRestController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dressDtoRestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @Test
    void shouldFindAllWithNumberOfOrders() throws Exception {
        DressDto firstDressDto = new DressDto();
        firstDressDto.setDressId(1);
        firstDressDto.setDressName("First dress_dto");
        firstDressDto.setNumberOfOrders(5);
        DressDto secondDressDto = new DressDto();
        secondDressDto.setDressId(2);
        secondDressDto.setDressName("Second dress_dto");
        List<DressDto> dressDtos = Arrays.asList(firstDressDto, secondDressDto);

        when(dressDtoService.findAllWithNumberOfOrders()).thenReturn(dressDtos);

        mockMvc.perform(get(DRESS_DTOS_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(dressDtos)));

        verify(dressDtoService, times(1)).findAllWithNumberOfOrders();
        verifyNoMoreInteractions(dressDtoService);
    }
}