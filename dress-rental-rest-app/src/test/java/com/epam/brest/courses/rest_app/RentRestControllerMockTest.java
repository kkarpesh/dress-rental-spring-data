package com.epam.brest.courses.rest_app;

import com.epam.brest.courses.model.Rent;
import com.epam.brest.courses.rest_app.controller.RentRestController;
import com.epam.brest.courses.rest_app.exception_handler.ExceptionRestControllerAdviser;
import com.epam.brest.courses.service_api.RentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RentRestControllerMockTest {

    private MockMvc mockMvc;

    private static final String RENTS_ENDPOINT = "/rents";
    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Mock
    private RentService rentService;

    @InjectMocks
    private RentRestController rentRestController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(rentRestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(new ExceptionRestControllerAdviser())
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(rentService);
    }

    @Test
    void shouldFindAll() throws Exception {
        Rent firstRent = new Rent();
        firstRent.setRentId(1);
        firstRent.setClient("First client");
        firstRent.setRentDate(LocalDate.of(2020,1,1));
        firstRent.setDressId(1);
        Rent secondRent = new Rent();
        secondRent.setRentId(2);
        secondRent.setClient("Second client");
        secondRent.setRentDate(LocalDate.of(2021,1,1));
        secondRent.setDressId(2);
        List<Rent> rents = Arrays.asList(firstRent, secondRent);

        when(rentService.findAll()).thenReturn(rents);

        mockMvc.perform(get(RENTS_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(rents)));

        verify(rentService, times(1)).findAll();

    }

    @Test
    void shouldFindByExistingId() throws Exception {
        Integer id = 1;
        Rent rent = new Rent();
        rent.setRentId(id);
        rent.setClient("First client");
        rent.setRentDate(LocalDate.of(2020,1,1));
        rent.setDressId(1);

        when(rentService.findById(1)).thenReturn(Optional.of(rent));

        mockMvc.perform(get(RENTS_ENDPOINT + "/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(rent)));

        verify(rentService, times(1)).findById(id);
    }

    @Test
    void shouldFindByNonExistedId() throws Exception {
        Integer id = 1;

        when(rentService.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get(RENTS_ENDPOINT + "/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(rentService, times(1)).findById(id);
    }

    @Test
    void shouldCreateNewRent() throws Exception {
        Integer id = 1;
        Rent rent = new Rent();
        rent.setRentId(id);
        rent.setClient("First client");
        rent.setRentDate(LocalDate.of(2020,1,1));
        rent.setDressId(1);

        when(rentService.create(any())).thenReturn(id);

        mockMvc.perform(post(RENTS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(rent))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(id)));

        verify(rentService, times(1)).create(any());
    }

    @Test
    void shouldReturnInternalServerErrorWhenCreateRentWithInvalidData() throws Exception {
        when(rentService.create(any())).thenThrow(new DataIntegrityViolationException("Error"));

        mockMvc.perform(post(RENTS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Rent()))
        )
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(rentService, times(1)).create(any());
    }

    @Test
    void shouldReturnUnprocessableEntityWhenCreateExistingRent() throws Exception {
        when(rentService.create(any())).thenThrow(new IllegalArgumentException());

        mockMvc.perform(post(RENTS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Rent()))
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(rentService, times(1)).create(any());
    }

    @Test
    void shouldUpdateRent() throws Exception {
        when(rentService.update(any())).thenReturn(1);

        mockMvc.perform(put(RENTS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Rent()))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(1)));

        verify(rentService, times(1)).update(any());
    }

    @Test
    void shouldReturnInternalServerErrorWhenUpdateRentWithInvalidData() throws Exception {
        when(rentService.update(any())).thenThrow(new IllegalArgumentException());

        mockMvc.perform(put(RENTS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Rent()))
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(rentService, times(1)).update(any());
    }

    @Test
    void shouldReturnUnprocessableEntityWhenUpdateWithExistingRent() throws Exception {
        when(rentService.update(any())).thenThrow(new DataIntegrityViolationException("Error"));

        mockMvc.perform(put(RENTS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Rent()))
        )
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(rentService, times(1)).update(any());
    }


    @Test
    void shouldDeleteRent() throws Exception {
        Integer id = 1;
        when(rentService.delete(id)).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.delete(RENTS_ENDPOINT + "/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(1)));

        verify(rentService, times(1)).delete(id);
    }

    @Test
    void shouldCheckNameAlreadyExists() throws Exception {
        Boolean result = true;
        when(rentService.hasDressAlreadyBeenRentedForThisDate(any())).thenReturn(result);

        mockMvc.perform(post(RENTS_ENDPOINT + "/isExists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Rent()))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(result)));

        verify(rentService, times(1)).hasDressAlreadyBeenRentedForThisDate(any());
    }
}