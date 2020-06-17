package com.epam.brest.courses.service;

import com.epam.brest.courses.model.Rent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class RentServiceImplMockTest {
//
//    @Mock
//    private RentDao rentDao;
//
//    @InjectMocks
//    private RentServiceImpl rentService;
//
//    @Test
//    void shouldFindAllRents() {
//        when(rentDao.findAll()).thenReturn(Collections.singletonList(new Rent()));
//
//        List<Rent> rents = rentService.findAll();
//        assertNotNull(rents);
//        assertTrue(rents.size() > 0);
//
//        verify(rentDao, times(1)).findAll();
//    }
//
////    @Test
////    void shouldFindRentWithExistingId() {
////        Rent rent = new Rent();
////        rent.setRentId(1);
////        rent.setClient("Client");
////        rent.setRentDate(LocalDate.now());
////        rent.setDressId(1);
////
////        when(rentDao.findById(anyInt())).thenReturn(Optional.of(rent));
////
////        Optional<Rent> optionalRent = rentService.findById(rent.getRentId());
////        assertTrue(optionalRent.isPresent());
////        assertSame(rent, optionalRent.get());
////
////        verify(rentDao, times(1)).findById(rent.getRentId());
////    }
//
//    @Test
//    void shouldReturnNullWhenFindByNonexistentId() {
//        when(rentDao.findById(anyInt())).thenReturn(Optional.empty());
//
//        Optional<Rent> optionalRent = rentService.findById(anyInt());
//        assertTrue(optionalRent.isEmpty());
//
//        verify(rentDao, times(1)).findById(anyInt());
//    }
//
//    @Test
//    void shouldCreateNewRent() {
//        Integer rentId = 1;
//        when(rentDao.create(any())).thenReturn(rentId);
//
//        Integer id = rentService.create(any());
//
//        assertNotNull(id);
//        assertEquals(rentId, id);
//
//        verify(rentDao, times(1)).create(any());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenCreateNewRentIfDressWasRentedOnThisDay() {
//        when(rentDao.create(any())).thenThrow(IllegalArgumentException.class);
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            rentService.create(any());
//        });
//
//        verify(rentDao, times(1)).create(any());
//    }
//
//    @Test
//    void shouldUpdatedRent() {
//        when(rentDao.update(any())).thenReturn(1);
//
//        Integer result = rentService.update(any());
//
//        assertNotNull(result);
//        assertEquals(1, (int) result);
//
//        verify(rentDao, times(1)).update(any());
//    }
//
//    @Test
//    void shouldThrowExceptionWhenUpdateRentIfDressWasRentedOnThisDay() {
//        when(rentDao.update(any())).thenThrow(IllegalArgumentException.class);
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            rentService.update(any());
//        });
//
//        verify(rentDao, times(1)).update(any());
//    }
//
//    @Test
//    void shouldDeleteRent() {
//        when(rentDao.delete(anyInt())).thenReturn(1);
//
//        Integer result = rentService.delete(anyInt());
//
//        assertNotNull(result);
//        assertEquals(1, (int) result);
//
//        verify(rentDao, times(1)).delete(anyInt());
//    }
//
//    @Test
//    void hasDressAlreadyBeenRentedForThisDateReturnTrue() {
//        when(rentDao.hasDressAlreadyBeenRentedForThisDate(any())).thenReturn(true);
//
//        assertTrue(rentService.hasDressAlreadyBeenRentedForThisDate(any()));
//
//        verify(rentDao, times(1)).hasDressAlreadyBeenRentedForThisDate(any());
//    }
//
//    @Test
//    void hasDressAlreadyBeenRentedForThisDateReturnFalse() {
//        when(rentDao.hasDressAlreadyBeenRentedForThisDate(any())).thenReturn(false);
//
//        assertFalse(rentService.hasDressAlreadyBeenRentedForThisDate(any()));
//
//        verify(rentDao, times(1)).hasDressAlreadyBeenRentedForThisDate(any());
//    }
//}