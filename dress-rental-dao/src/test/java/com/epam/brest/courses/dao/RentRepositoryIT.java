package com.epam.brest.courses.dao;

import com.epam.brest.courses.dao.config.DaoTestConfig;
import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.model.Rent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DaoTestConfig.class)
class RentRepositoryIT {

    private final RentRepository rentRepository;

    private final DressRepository dressRepository;

    @Autowired
    RentRepositoryIT(RentRepository rentRepository, DressRepository dressRepository) {
        this.rentRepository = rentRepository;
        this.dressRepository = dressRepository;
    }

    @Test
    void shouldCountByDress() {
        Dress dress = new Dress();
        dress.setDressId(1);

        assertEquals(2, rentRepository.countByDress(dress));

    }

    @Test
    void shouldFindByDateBetween() {
        LocalDate dateFrom = LocalDate.of(2020, 1, 1);
        LocalDate dateTo = LocalDate.of(2021, 1, 1);
        List<Rent> rents = rentRepository.findByRentDateBetween(dateFrom, dateTo);

        assertNotNull(rents);
    }

    @Test
    void shouldFindByDateBetweenEmptyList() {
        LocalDate dateFrom = LocalDate.of(2019, 1, 1);
        LocalDate dateTo = LocalDate.of(2019, 12, 31);
        List<Rent> rents = rentRepository.findByRentDateBetween(dateFrom, dateTo);

        assertTrue(rents.isEmpty());
    }

    @Test
    void shouldFindByDateBetween2Items() {
        LocalDate dateFrom = LocalDate.of(2020, 1, 1);
        LocalDate dateTo = LocalDate.of(2020, 3, 1);
        List<Rent> rents = rentRepository.findByRentDateBetween(dateFrom, dateTo);

        assertEquals(2, rents.size());
    }

    @Test
    void shouldFindByRentDateAndDress() {
        LocalDate rentDate = LocalDate.of(2020,1,1);
        Dress dress = new Dress();
        dress.setDressId(1);

        Optional<Rent> rent = rentRepository.findByRentDateAndDress(rentDate, dress);

        assertTrue(rent.isPresent());
    }

    @Test
    void shouldFindByRentDateAndDressNotRentId() {
        Dress dress = new Dress();
        dress.setDressName("Dress");
        Dress savedDress = dressRepository.save(dress);

        Rent rent = new Rent();
        rent.setClient("Client");
        rent.setRentDate(LocalDate.now());
        rent.setDress(savedDress);

        Rent savedRent = rentRepository.save(rent);
        System.out.println(savedRent);

        Optional<Rent> optionalRent = rentRepository.findAnotherSameRent(
                savedRent.getRentDate(),
                savedRent.getDress().getDressName(),
                savedRent.getRentId());

        assertTrue(optionalRent.isEmpty());
    }
}