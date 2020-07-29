package com.epam.brest.courses.service;

import com.epam.brest.courses.dao.DressRepository;
import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.model.Rent;
import com.epam.brest.courses.model.dto.DressDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DressServiceImplMockTest {

    @Mock
    private DressRepository dressRepository;

    @InjectMocks
    private DressServiceImpl dressService;

    @Test
    void shouldFindAllDress() {
        Dress dress = new Dress();
        dress.setDressId(1);
        dress.setDressName("Dress");
        dress.setRents(Collections.EMPTY_SET);
        when(dressRepository.findAll()).thenReturn(Collections.singletonList(dress));

        List<DressDto> dresses = dressService.findAllWithNumberOfOrders();
        assertNotNull(dresses);
        assertTrue(dresses.size() > 0);

        verify(dressRepository, times(1)).findAll();
    }

    @Test
    void shouldFindDressWithExistingId() {
        Integer dressId = 1;
        String dressName = "Dress";
        Dress dress = new Dress();
        dress.setDressId(dressId);
        dress.setDressName(dressName);
        dress.setRents(Collections.EMPTY_SET);

        when(dressRepository.findById(anyInt())).thenReturn(Optional.of(dress));

        Optional<DressDto> optionalDress = dressService.findById(dressId);
        assertTrue(optionalDress.isPresent());
        assertEquals(dress.getDressId(), optionalDress.get().getDressId());
        assertEquals(dress.getDressName(), optionalDress.get().getDressName());

        verify(dressRepository, times(1)).findById(dressId);
    }

    @Test
    void shouldReturnNullWhenFindByNonexistentId() {
        when(dressRepository.findById(anyInt())).thenReturn(Optional.empty());

        Optional<DressDto> optionalDress = dressService.findById(anyInt());
        assertTrue(optionalDress.isEmpty());

        verify(dressRepository, times(1)).findById(anyInt());
    }

    @Test
    void shouldCreateNewDress() {
        Dress dress = new Dress();
        dress.setDressName("Dress");
        Dress savedDress = new Dress();
        savedDress.setDressId(1);
        savedDress.setDressName(dress.getDressName());
        when((dressRepository.findByDressName("Dress"))).thenReturn(Optional.empty());
        when(dressRepository.save(any())).thenReturn(savedDress);

        DressDto dressDto = new DressDto();
        dressDto.setDressName(dress.getDressName());
        Integer id = dressService.createOrUpdate(dressDto);

        assertNotNull(id);
        assertEquals(savedDress.getDressId(), id);

        verify(dressRepository, times(1)).save(any());
    }

    @Test
    void shouldThrowExceptionWhenCreateNewDressWithExistingName() {
        Dress dress = new Dress();
        dress.setDressName("Dress");
        when((dressRepository.findByDressName("Dress"))).thenReturn(Optional.of(dress));
        DressDto dressDto = new DressDto();
        dressDto.setDressId(dress.getDressId());
        dressDto.setDressName(dress.getDressName());

        assertThrows(IllegalArgumentException.class, () -> {
            dressService.createOrUpdate(dressDto);
        });

        verify(dressRepository, times(1)).findByDressName("Dress");
    }

    @Test
    void shouldDeleteDressThatDoesNotHaveRents() {
        Dress dress = new Dress();
        dress.setDressId(1);
        dress.setDressName("Dress");
        dress.setRents(Collections.EMPTY_SET);
        when(dressRepository.findById(1)).thenReturn(Optional.of(dress));
        doNothing().when(dressRepository).deleteById(1);

        Integer result = dressService.delete(1);

        assertNotNull(result);
        assertEquals(1, (int) result);

        verify(dressRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void shouldThrowExceptionWhenDeleteDressWithRents() {
        Dress dress = new Dress();
        dress.setRents(Set.of(new Rent()));
        when(dressRepository.findById(1)).thenReturn(Optional.of(dress));

        assertThrows(UnsupportedOperationException.class, () -> {
            dressService.delete(1);
        });

        verify(dressRepository, times(1)).findById(1);
    }

    @Test
    void shouldThrowExceptionWhenDeleteNonExistedDress() {
        when(dressRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            dressService.delete(1);
        });

        verify(dressRepository, times(1)).findById(1);
    }

    @Test
    void isNameAlreadyExistReturnTrue() {
        Dress dress = new Dress();
        dress.setDressName("Dress");

        DressDto dressDto = new DressDto();
        dressDto.setDressName(dress.getDressName());
        when(dressRepository.findByDressName("Dress")).thenReturn(Optional.of(dress));

        assertTrue(dressService.isNameAlreadyExist(dressDto));

        verify(dressRepository, times(1)).findByDressName(any());
    }

    @Test
    void isNameAlreadyExistReturnFalseForCurrentDress() {

        DressDto dressDto = new DressDto();
        dressDto.setDressId(1);
        dressDto.setDressName("Dress");
        when(dressRepository.findByDressNameWhereDressIdNot(dressDto.getDressName(), dressDto.getDressId())).thenReturn(Optional.empty());

        assertFalse(dressService.isNameAlreadyExist(dressDto));

        verify(dressRepository, times(1)).findByDressNameWhereDressIdNot(dressDto.getDressName(), dressDto.getDressId());
    }

    @Test
    void isNameAlreadyExistReturnFalse() {
        DressDto dressDto = new DressDto();
        dressDto.setDressName("Dress");
        when(dressRepository.findByDressName("Dress")).thenReturn(Optional.empty());

        assertFalse(dressService.isNameAlreadyExist(dressDto));

        verify(dressRepository, times(1)).findByDressName(any());
    }

    @Test
    void isDressHasRentsReturnTrue() {
        Dress dress = new Dress();
        dress.setRents(Set.of(new Rent()));
        when(dressRepository.findById(anyInt())).thenReturn(Optional.of(dress));

        assertTrue(dressService.isDressHasRents(anyInt()));

        verify(dressRepository, times(1)).findById(anyInt());
    }

    @Test
    void isDressHasRentsReturnFalse() {
        Dress dress = new Dress();
        dress.setRents(Collections.EMPTY_SET);
        when(dressRepository.findById(anyInt())).thenReturn(Optional.of(dress));

        assertFalse(dressService.isDressHasRents(anyInt()));

        verify(dressRepository, times(1)).findById(anyInt());
    }

    @Test
    void shouldThrowExceptionIsDressHasRentsReturnFalse() {
        when(dressRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            dressService.isDressHasRents(1);
        });

        verify(dressRepository, times(1)).findById(anyInt());
    }
}