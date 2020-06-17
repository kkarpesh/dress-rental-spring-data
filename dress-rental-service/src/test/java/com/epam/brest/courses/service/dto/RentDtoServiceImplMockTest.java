package com.epam.brest.courses.service.dto;

import com.epam.brest.courses.model.dto.RentDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class RentDtoServiceImplMockTest {
//
//    @Mock
//    private RentDtoDao rentDtoDao;
//
//    @InjectMocks
//    private RentDtoServiceImpl rentDtoService;
//
//    private static final LocalDate dateFrom = LocalDate.of(2020, 1, 1);
//    private static final LocalDate dateTo = LocalDate.of(2021, 1, 1);
//
//
//    @Test
//    void shouldFindAllWIthDressNameByDate() {
//        when(rentDtoDao.findAllWIthDressNameByDate(dateFrom, dateTo)).thenReturn(Collections.singletonList(new RentDto()));
//
//        List<RentDto> rentDtos = rentDtoService.findAllWIthDressNameByDate(dateFrom, dateTo);
//        assertNotNull(rentDtos);
//        assertTrue(rentDtos.size() > 0);
//
//        verify(rentDtoDao, times(1)).findAllWIthDressNameByDate(dateFrom, dateTo);
//    }
//
//    @Test
//    void shouldThrowsExceptionIfDateFromIsAfterDateTo() {
//        when(rentDtoDao.findAllWIthDressNameByDate(dateTo, dateFrom)).thenThrow(IllegalArgumentException.class);
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            rentDtoService.findAllWIthDressNameByDate(dateTo, dateFrom);
//        });
//
//        verify(rentDtoDao, times(1)).findAllWIthDressNameByDate(dateTo, dateFrom);
//    }
//
//}
