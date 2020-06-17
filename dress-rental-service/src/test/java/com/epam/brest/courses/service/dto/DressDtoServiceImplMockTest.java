package com.epam.brest.courses.service.dto;

import com.epam.brest.courses.dao.DressRepository;
import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.model.Rent;
import com.epam.brest.courses.model.dto.DressDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
//class DressDtoServiceImplMockTest {
//
//    @Mock
//    private DressRepository dressRepository;
//
//    @InjectMocks
//    private DressDtoServiceImpl dressDtoService;
//
//    @Test
//    void shouldFindAllWithNumberOfOrders() {
//        when(dressDtoDao.findAllWithNumberOfOrders()).thenReturn(Collections.singletonList(new DressDto()));
//
//        List<DressDto> dressDtos =  dressDtoService.findAllWithNumberOfOrders();
//
//        assertNotNull(dressDtos);
//        assertTrue(dressDtos.size()>0);
//
//        verify(dressDtoDao, times(1)).findAllWithNumberOfOrders();
//    }
//}