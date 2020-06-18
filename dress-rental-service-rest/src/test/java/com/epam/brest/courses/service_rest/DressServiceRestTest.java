package com.epam.brest.courses.service_rest;

import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.model.dto.DressDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.epam.brest.courses.constants.DressConstants.DRESS_NAME_SIZE;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceRestTestConfig.class)
class DressServiceRestTest {

    public static final String DRESSES_URL = "http://localhost:8088/dresses";

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    private ObjectMapper mapper = new ObjectMapper();

    DressServiceRest dressService;

    @BeforeEach
    public void before() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
        dressService = new DressServiceRest(DRESSES_URL, restTemplate);
    }

    @Test
    void shouldFindAllDressesWithNumberOfOrders() throws JsonProcessingException {
        // given
        mockServer.expect(ExpectedCount.once(), requestTo(DRESSES_URL))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(Arrays.asList(new DressDto(), new DressDto())))
                );

        //when
        List<DressDto> dresses = dressService.findAllWithNumberOfOrders();

        // then
        mockServer.verify();
        assertNotNull(dresses);
        assertTrue(dresses.size() > 0);
        assertEquals(DressDto.class, dresses.get(0).getClass());
    }

    @Test
    void shouldFindDressById() throws JsonProcessingException {
        // given
        Integer id = 1;
        DressDto dress = new DressDto();
        dress.setDressId(id);
        dress.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));
        dress.setNumberOfOrders(2);

        mockServer.expect(ExpectedCount.once(), requestTo(DRESSES_URL + "/" + id))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(dress))
                );

        // when
        Optional<DressDto> optionalDress = dressService.findById(id);

        // then
        mockServer.verify();
        assertTrue(optionalDress.isPresent());
        assertEquals(dress.getDressId(), optionalDress.get().getDressId());
        assertEquals(dress.getDressName(), optionalDress.get().getDressName());
        assertEquals(dress.getNumberOfOrders(), optionalDress.get().getNumberOfOrders());
    }

    @Test
    void shouldCreateNewDress() throws JsonProcessingException {
        // given
        DressDto dress = new DressDto();
        dress.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));

        mockServer.expect(ExpectedCount.once(), requestTo(DRESSES_URL))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );

        // when
        Integer id = dressService.createOrUpdate(dress);

        // then
        mockServer.verify();
        assertNotNull(id);
    }

//    @Test
//    void shouldUpdateDress() throws JsonProcessingException {
//        // given
//        Integer id = 1;
//        DressDto dress = new DressDto();
//        dress.setDressId(id);
//        dress.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));
//
//        mockServer.expect(ExpectedCount.once(), requestTo(DRESSES_URL))
//                .andExpect(method(HttpMethod.PUT))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString("1"))
//                );
//
//        mockServer.expect(ExpectedCount.once(), requestTo(DRESSES_URL + "/" + id))
//                .andExpect(method(HttpMethod.GET))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString(dress))
//                );
//
//        Integer result = dressService.createOrUpdate(dress);
//        Optional<DressDto> updatedDress = dressService.findById(id);
//
//        // then
//        mockServer.verify();
//        assertEquals(1, (int) result);
//        assertTrue(updatedDress.isPresent());
//        assertEquals(dress.getDressId(), updatedDress.get().getDressId());
//        assertEquals(dress.getDressName(), updatedDress.get().getDressName());
//    }

    @Test
    void shouldDeleteDress() throws JsonProcessingException {
        // given
        Integer id = 1;
        mockServer.expect(ExpectedCount.once(), requestTo(DRESSES_URL + "/" + id))
                .andExpect(method(HttpMethod.DELETE))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString("1"))
                );

        // when
        Integer result = dressService.delete(id);

        // then
        mockServer.verify();
        assertEquals(1, (int) result);
    }

    @Test
    void shouldCheckIsNameAlreadyExist() throws JsonProcessingException {
        //given
        mockServer.expect(ExpectedCount.once(), requestTo(DRESSES_URL + "/isExists"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(true))
                );

        // when
        Boolean result = dressService.isNameAlreadyExist(new DressDto());

        // then
        mockServer.verify();
        assertTrue(result);
    }

    @Test
    void shouldCheckIsNameNotExist() throws JsonProcessingException {
        //given
        mockServer.expect(ExpectedCount.once(), requestTo(DRESSES_URL + "/isExists"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(false))
                );

        // when
        Boolean result = dressService.isNameAlreadyExist(new DressDto());

        // then
        mockServer.verify();
        assertFalse(result);
    }

    @Test
    void shouldCheckIsDressHasRents() throws JsonProcessingException {
        //given
        Integer id = 1;
        mockServer.expect(ExpectedCount.once(), requestTo(DRESSES_URL + "/" + id + "/hasRents"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(true))
                );

        // when
        Boolean result = dressService.isDressHasRents(id);

        // then
        mockServer.verify();
        assertTrue(result);
    }

    @Test
    void shouldCheckIsDressHasNotRents() throws JsonProcessingException {
        //given
        Integer id = 1;
        mockServer.expect(ExpectedCount.once(), requestTo(DRESSES_URL + "/" + id + "/hasRents"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(false))
                );

        // when
        Boolean result = dressService.isDressHasRents(id);

        // then
        mockServer.verify();
        assertFalse(result);
    }
}
