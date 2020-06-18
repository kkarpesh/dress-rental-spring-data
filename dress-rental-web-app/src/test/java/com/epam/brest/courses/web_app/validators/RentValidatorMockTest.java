package com.epam.brest.courses.web_app.validators;

import com.epam.brest.courses.model.dto.RentDto;
import com.epam.brest.courses.service_api.RentService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;

import static com.epam.brest.courses.constants.RentConstants.RENT_CLIENT_SIZE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentValidatorMockTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RentValidatorMockTest.class);
    private BindingResult result;

    @Mock
    private RentService rentService;

    @Mock
    private RentDto rent;

    @InjectMocks
    private RentValidator rentValidator;

    @BeforeEach
    void setUp() {
        result = new BeanPropertyBindingResult(rent, "rent");
    }

    @Test
    void shouldSupportsValidation() {
        LOGGER.debug("shouldSupportsValidation()");
        assertTrue(rentValidator.supports(rent.getClass()));
    }

    @Test
    void shouldRejectNullClient() {
        LOGGER.debug("shouldRejectNullClient");
        // given
        when(rentService.isDressRented(rent)).thenReturn(false);
        when(rent.getClient()).thenReturn(null);

        //when
        rentValidator.validate(rent, result);

        // then
        assertTrue(result.hasErrors());
    }

    @Test
    void shouldRejectLargeClient() {
        LOGGER.debug("shouldRejectLargeClient()");
        // given
        when(rentService.isDressRented(rent)).thenReturn(false);
        when(rent.getClient()).thenReturn(RandomStringUtils.randomAlphabetic(RENT_CLIENT_SIZE + 1));

        //when
        rentValidator.validate(rent, result);

        // then
        assertTrue(result.hasErrors());
    }

    @Test
    void shouldRejectEmptyClient() {
        LOGGER.debug("shouldRejectEmptyClient()");
        // given
        when(rentService.isDressRented(rent)).thenReturn(false);
        when(rent.getClient()).thenReturn("");

        //when
        rentValidator.validate(rent, result);

        // then
        assertTrue(result.hasErrors());
    }

    @Test
    void shouldRejectNullRentDate() {
        LOGGER.debug("shouldRejectNullRentDate");
        // given
        when(rentService.isDressRented(rent)).thenReturn(false);
        when(rent.getRentDate()).thenReturn(null);

        //when
        rentValidator.validate(rent, result);

        // then
        assertTrue(result.hasErrors());
    }

    @Test
    void shouldRejectDuplicateRent() {
        LOGGER.debug("shouldRejectDuplicateRent() ");
        // given

        when(rent.getClient()).thenReturn(RandomStringUtils.randomAlphabetic(RENT_CLIENT_SIZE));
        when(rent.getRentDate()).thenReturn(LocalDate.now());
        when(rentService.isDressRented(rent)).thenReturn(true);

        //when
        rentValidator.validate(rent, result);

        // then
        assertTrue(result.hasErrors());
    }

    @Test
    void shouldValidateRent() {
        LOGGER.debug("shouldValidateRent()");
        // given
        when(rentService.isDressRented(rent)).thenReturn(false);
        when(rent.getClient()).thenReturn(RandomStringUtils.randomAlphabetic(RENT_CLIENT_SIZE));
        when(rent.getRentDate()).thenReturn(LocalDate.now());

        //when
        rentValidator.validate(rent, result);

        // then
        assertFalse(result.hasErrors());
    }

}