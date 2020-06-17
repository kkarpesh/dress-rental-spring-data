package com.epam.brest.courses.web_app.validators;

import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.service_api.DressService;
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

import static com.epam.brest.courses.constants.DressConstants.DRESS_NAME_SIZE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class DressValidatorMockTest {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(DressValidatorMockTest.class);
//    private BindingResult result;
//
//    @Mock
//    private DressService dressService;
//
//    @Mock
//    private Dress dress;
//
//    @InjectMocks
//    private DressValidator dressValidator;
//
//    @BeforeEach
//    void setUp() {
//        result = new BeanPropertyBindingResult(dress, "dress");
//    }
//
//    @Test
//    void shouldSupportsValidation() {
//        LOGGER.debug("shouldSupportsValidation()");
//        assertTrue(dressValidator.supports(dress.getClass()));
//    }
//
//    @Test
//    void shouldRejectNullDressName() {
//        LOGGER.debug("shouldRejectNullDressName()");
//        // given
//        when(dressService.isNameAlreadyExist(dress)).thenReturn(false);
//        when(dress.getDressName()).thenReturn(null);
//
//        //when
//        dressValidator.validate(dress, result);
//
//        // then
//        assertTrue(result.hasErrors());
//    }
//
//    @Test
//    void shouldRejectLargeDressName() {
//        LOGGER.debug("shouldRejectLargeDressName()");
//        // given
//        when(dressService.isNameAlreadyExist(dress)).thenReturn(false);
//        when(dress.getDressName()).thenReturn(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE + 1));
//
//        //when
//        dressValidator.validate(dress, result);
//
//        // then
//        assertTrue(result.hasErrors());
//    }
//
//    @Test
//    void shouldRejectEmptyDressName() {
//        LOGGER.debug("shouldRejectEmptyDressName()");
//        // given
//        when(dressService.isNameAlreadyExist(dress)).thenReturn(false);
//        when(dress.getDressName()).thenReturn("");
//
//        //when
//        dressValidator.validate(dress, result);
//
//        // then
//        assertTrue(result.hasErrors());
//    }
//
//    @Test
//    void shouldRejectDuplicateDressName() {
//        LOGGER.debug("shouldRejectDuplicateDressName() ");
//        // given
//        when(dressService.isNameAlreadyExist(dress)).thenReturn(true);
//        when(dress.getDressName()).thenReturn(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));
//
//        //when
//        dressValidator.validate(dress, result);
//
//        // then
//        assertTrue(result.hasErrors());
//    }
//
//    @Test
//    void shouldValidateDressName() {
//        LOGGER.debug("shouldValidateDressName()");
//        // given
//        when(dressService.isNameAlreadyExist(dress)).thenReturn(false);
//        when(dress.getDressName()).thenReturn(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));
//
//        //when
//        dressValidator.validate(dress, result);
//
//        // then
//        assertFalse(result.hasErrors());
//    }
//
//
//}