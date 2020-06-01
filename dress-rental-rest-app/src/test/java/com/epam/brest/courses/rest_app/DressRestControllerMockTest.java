package com.epam.brest.courses.rest_app;

import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.rest_app.controller.DressRestController;
import com.epam.brest.courses.rest_app.exception_handler.ExceptionRestControllerAdviser;
import com.epam.brest.courses.service_api.DressService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DressRestControllerMockTest {

    private MockMvc mockMvc;

    private static final String DRESSES_ENDPOINT = "/dresses";
    ObjectMapper mapper = new ObjectMapper();

    @Mock
    private DressService dressService;

    @InjectMocks
    private DressRestController dressRestController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dressRestController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .setControllerAdvice(new ExceptionRestControllerAdviser())
                .alwaysDo(MockMvcResultHandlers.print())
                .build();
    }

    @AfterEach
    void afterEach() {
        verifyNoMoreInteractions(dressService);
    }

    @Test
    void shouldFindAll() throws Exception {
        Dress firstDress = new Dress();
        firstDress.setDressId(1);
        firstDress.setDressName("First dress");
        Dress secondDress = new Dress();
        secondDress.setDressId(2);
        secondDress.setDressName("Second dress");
        List<Dress> dresses = Arrays.asList(firstDress, secondDress);

        when(dressService.findAll()).thenReturn(dresses);

        mockMvc.perform(get(DRESSES_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(dresses)));

        verify(dressService, times(1)).findAll();

    }

    @Test
    void shouldFindByExistingId() throws Exception {
        Integer id = 1;
        Dress dress = new Dress();
        dress.setDressId(id);
        dress.setDressName("First dress");

        when(dressService.findById(1)).thenReturn(Optional.of(dress));

        mockMvc.perform(get(DRESSES_ENDPOINT + "/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(dress)));

        verify(dressService, times(1)).findById(id);
    }

    @Test
    void shouldFindByNonExistedId() throws Exception {
        Integer id = 1;

        when(dressService.findById(1)).thenReturn(Optional.empty());

        mockMvc.perform(get(DRESSES_ENDPOINT + "/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(dressService, times(1)).findById(id);
    }

    @Test
    void shouldCreateNewDress() throws Exception {
        Integer id = 1;
        Dress dress = new Dress();
        dress.setDressName("First dress");

        when(dressService.create(any())).thenReturn(id);

        mockMvc.perform(post(DRESSES_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(dress))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(id)));

        verify(dressService, times(1)).create(any());
    }

    @Test
    void shouldReturnInternalServerErrorWhenCreateDressWithInvalidData() throws Exception {
        when(dressService.create(any())).thenThrow(new DataIntegrityViolationException("Error"));

        mockMvc.perform(post(DRESSES_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Dress()))
        )
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(dressService, times(1)).create(any());
    }

    @Test
    void shouldReturnUnprocessableEntityWhenCreateExistingDress() throws Exception {
        when(dressService.create(any())).thenThrow(new IllegalArgumentException());

        mockMvc.perform(post(DRESSES_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Dress()))
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(dressService, times(1)).create(any());
    }

    @Test
    void shouldUpdateDress() throws Exception {
        when(dressService.update(any())).thenReturn(1);

        mockMvc.perform(put(DRESSES_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Dress()))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(1)));

        verify(dressService, times(1)).update(any());
    }

    @Test
    void shouldReturnInternalServerErrorWhenUpdateDressWithInvalidData() throws Exception {
        when(dressService.update(any())).thenThrow(new IllegalArgumentException());

        mockMvc.perform(put(DRESSES_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Dress()))
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(dressService, times(1)).update(any());
    }

    @Test
    void shouldReturnUnprocessableEntityWhenUpdateWithExistingDress() throws Exception {
        when(dressService.update(any())).thenThrow(new DataIntegrityViolationException("Error"));

        mockMvc.perform(put(DRESSES_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Dress()))
        )
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(dressService, times(1)).update(any());
    }


    @Test
    void shouldDeleteDress() throws Exception {
        Integer id = 1;
        when(dressService.delete(id)).thenReturn(1);

        mockMvc.perform(MockMvcRequestBuilders.delete(DRESSES_ENDPOINT + "/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(1)));

        verify(dressService, times(1)).delete(id);
    }

    @Test
    void shouldReturnInternalServerErrorThenDeleteDressWithRents() throws Exception {
        Integer id = 1;
        when(dressService.delete(id)).thenThrow(new UnsupportedOperationException());

        mockMvc.perform(MockMvcRequestBuilders.delete(DRESSES_ENDPOINT + "/" + id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(dressService, times(1)).delete(id);
    }

    @Test
    void shouldCheckNameAlreadyExists() throws Exception {
        Boolean result = true;
        when(dressService.isNameAlreadyExist(any())).thenReturn(result);

        mockMvc.perform(post(DRESSES_ENDPOINT + "/isExists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new Dress()))
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(result)));

        verify(dressService, times(1)).isNameAlreadyExist(any());
    }

    @Test
    void shouldCheckIsDressHasRents() throws Exception {
        Boolean result = true;
        Integer id = 1;
        when(dressService.isDressHasRents(id)).thenReturn(result);

        mockMvc.perform(get(DRESSES_ENDPOINT + "/" + id + "/hasRents")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(mapper.writeValueAsString(result)));

        verify(dressService, times(1)).isDressHasRents(any());
    }

}