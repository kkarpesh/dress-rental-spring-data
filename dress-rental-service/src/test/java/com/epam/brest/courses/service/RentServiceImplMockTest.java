package com.epam.brest.courses.service;

import com.epam.brest.courses.dao.DressRepository;
import com.epam.brest.courses.dao.RentRepository;
import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.model.Rent;
import com.epam.brest.courses.model.dto.RentDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentServiceImplMockTest {

    @Mock
    private RentRepository rentRepository;

    @Mock
    private DressRepository dressRepository;

    @InjectMocks
    private RentServiceImpl rentService;

    @Test
    void shouldFindAllRents() {
        Rent rent = new Rent();
        rent.setRentId(1);
        rent.setClient("Client");
        rent.setRentDate(LocalDate.of(2020, 1, 1));
        rent.setDress(new Dress());

        LocalDate date = LocalDate.of(2020, 1, 1);
        when(rentRepository.findByRentDateBetween(date, date)).thenReturn(Collections.singletonList(rent));

        List<RentDto> rents = rentService.findAllByDate(date, date);

        assertNotNull(rents);
        assertTrue(rents.size() > 0);

        verify(rentRepository, times(1)).findByRentDateBetween(date, date);
    }

    @Test
    void shouldFindRentWithExistingId() {
        Rent rent = new Rent();
        rent.setRentId(1);
        rent.setClient("Client");
        rent.setRentDate(LocalDate.now());

        Dress dress = new Dress();
        dress.setDressName("Dress");
        rent.setDress(dress);

        when(rentRepository.findById(rent.getRentId())).thenReturn(Optional.of(rent));

        Optional<RentDto> optionalRent = rentService.findById(rent.getRentId());
        assertTrue(optionalRent.isPresent());
        assertEquals(rent.getRentId(), optionalRent.get().getRentId());
        assertEquals(rent.getClient(), optionalRent.get().getClient());
        assertEquals(rent.getRentDate(), optionalRent.get().getRentDate());
        assertEquals(rent.getDress().getDressName(), optionalRent.get().getDressName());

        verify(rentRepository, times(1)).findById(rent.getRentId());
    }

    @Test
    void shouldReturnNullWhenFindByNonexistentId() {
        when(rentRepository.findById(anyInt())).thenReturn(Optional.empty());

        Optional<RentDto> optionalRent = rentService.findById(anyInt());
        assertTrue(optionalRent.isEmpty());

        verify(rentRepository, times(1)).findById(anyInt());
    }

    @Test
    void shouldCreateNewRent() {
        Rent rent = new Rent();
        rent.setRentId(1);
        rent.setClient("Client");
        rent.setRentDate(LocalDate.now());

        Dress dress = new Dress();
        dress.setDressId(1);
        dress.setDressName("Dress");
        rent.setDress(dress);
        when(dressRepository.findByDressName(anyString())).thenReturn(Optional.of(dress));
        when(rentRepository.findByRentDateAndDress(any(), any())).thenReturn(Optional.empty());
        when(rentRepository.save(any())).thenReturn(rent);

        RentDto rentDto = new RentDto();
        rentDto.setRentId(rent.getRentId());
        rentDto.setClient(rent.getClient());
        rentDto.setRentDate(rent.getRentDate());
        rentDto.setDressName(rent.getDress().getDressName());

        Integer id = rentService.createOrUpdate(rentDto);

        assertNotNull(id);
        assertEquals(rent.getRentId(), id);

        verify(rentRepository, times(1)).findByRentDateAndDress(any(), any());
        verify(rentRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowExceptionWhenCreateNewRentIfDressWasRentedOnThisDay() {
        Rent rent = new Rent();
        rent.setRentId(1);
        rent.setClient("Client");
        rent.setRentDate(LocalDate.now());

        Dress dress = new Dress();
        dress.setDressId(1);
        dress.setDressName("Dress");
        rent.setDress(dress);

        when(dressRepository.findByDressName(anyString())).thenReturn(Optional.of(dress));
        when(rentRepository.findByRentDateAndDress(any(), any())).thenReturn(Optional.of(rent));

        RentDto rentDto = new RentDto();
        rentDto.setRentId(rent.getRentId());
        rentDto.setClient(rent.getClient());
        rentDto.setRentDate(rent.getRentDate());
        rentDto.setDressName(rent.getDress().getDressName());

        assertThrows(IllegalArgumentException.class, () -> {
            rentService.createOrUpdate(rentDto);
        });

        verify(rentRepository, times(1)).findByRentDateAndDress(any(), any());
    }

    @Test
    void shouldThrowExceptionWhenCreateNewRentWithNonExistedDress() {
        RentDto rentDto = new RentDto();
        rentDto.setDressName("Dress");

        when(dressRepository.findByDressName("Dress")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            rentService.createOrUpdate(rentDto);
        });

        verify(dressRepository, times(1)).findByDressName("Dress");
    }

    @Test
    void shouldDeleteRent() {
        Rent rent = new Rent();
        rent.setRentId(1);
        when(rentRepository.findById(rent.getRentId())).thenReturn(Optional.of(rent));
        doNothing().when(rentRepository).deleteById(rent.getRentId());

        Integer result = rentService.delete(rent.getRentId());

        assertNotNull(result);
        assertEquals(1, (int) result);

        verify(rentRepository, times(1)).findById(rent.getRentId());
    }

    @Test
    void shouldThrowsExceptionWhenDeleteNonExistedRent() {
        Rent rent = new Rent();
        rent.setRentId(1);
        when(rentRepository.findById(rent.getRentId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            rentService.delete(rent.getRentId());
        });

        verify(rentRepository, times(1)).findById(1);
    }

    @Test
    void checkIsDressRentedReturnTrue() {
        RentDto rentDto = new RentDto();
        rentDto.setRentId(1);
        rentDto.setDressName("Dress");

        when(dressRepository.findByDressName(rentDto.getDressName())).thenReturn(Optional.of(new Dress()));
        when(rentRepository.findByRentDateAndDress(any(), any())).thenReturn(Optional.of(new Rent()));

        assertTrue(rentService.isDressRented(rentDto));

        verify(dressRepository, times(1)).findByDressName(rentDto.getDressName());
        verify(rentRepository, times(1)).findByRentDateAndDress(any(), any());
    }

    @Test
    void checkIsDressRentedReturnFalse() {
        RentDto rentDto = new RentDto();
        rentDto.setRentId(1);
        rentDto.setDressName("Dress");

        when(dressRepository.findByDressName(rentDto.getDressName())).thenReturn(Optional.of(new Dress()));
        when(rentRepository.findByRentDateAndDress(any(), any())).thenReturn(Optional.empty());

        assertFalse(rentService.isDressRented(rentDto));

        verify(dressRepository, times(1)).findByDressName(rentDto.getDressName());
        verify(rentRepository, times(1)).findByRentDateAndDress(any(), any());
    }

    @Test
    void checkIsDressRentedThrowsExceptionIfDressNonExist() {
        RentDto rentDto = new RentDto();
        rentDto.setRentId(1);
        rentDto.setDressName("Dress");

        when(dressRepository.findByDressName(rentDto.getDressName())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            rentService.isDressRented(rentDto);
        });

        verify(dressRepository, times(1)).findByDressName(rentDto.getDressName());
    }
}