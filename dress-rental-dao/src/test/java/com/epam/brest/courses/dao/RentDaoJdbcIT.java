package com.epam.brest.courses.dao;

import com.epam.brest.courses.dao.config.DaoTestConfig;
import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.model.Rent;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.epam.brest.courses.constants.DressConstants.DRESS_NAME_SIZE;
import static com.epam.brest.courses.constants.RentConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DaoTestConfig.class)
class RentDaoJdbcIT {

    private final RentDao rentDao;
    private final DressDao dressDao;

    @Autowired
    RentDaoJdbcIT(RentDao rentDao, DressDao dressDao) {
        this.rentDao = rentDao;
        this.dressDao = dressDao;
    }

    @Test
    void shouldFindAllRents() {
        // when
        List<Rent> rents = rentDao.findAll();

        //then
        assertNotNull(rents);
        assertTrue(rents.size() > 0);
    }

    @Test
    void shouldFindRentWithExistingId() {
        // given
        Dress dress = new Dress();
        dress.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));
        Integer dressId = dressDao.create(dress);

        Rent rent = new Rent();
        rent.setClient(RandomStringUtils.randomAlphabetic(RENT_CLIENT_SIZE));
        rent.setRentDate(LocalDate.now());
        rent.setDressId(dressId);
        Integer rentId = rentDao.create(rent);

        // when
        Optional<Rent> optionalRent = rentDao.findById(rentId);

        // then
        assertTrue(optionalRent.isPresent());
        assertEquals(rent.getClient(), optionalRent.get().getClient());
        assertEquals(rent.getRentDate(), optionalRent.get().getRentDate());
        assertEquals(rent.getDressId(), optionalRent.get().getDressId());
    }

    @Test
    void shouldReturnNullWhenFindByNonexistentId() {
        // given
        Dress dress = new Dress();
        dress.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));
        Integer dressId = dressDao.create(dress);

        Rent rent = new Rent();
        rent.setClient(RandomStringUtils.randomAlphabetic(RENT_CLIENT_SIZE));
        rent.setRentDate(LocalDate.now());
        rent.setDressId(dressId);
        Integer rentId = rentDao.create(rent);
        rentDao.delete(rentId);

        // when
        Optional<Rent> optionalRent = rentDao.findById(rentId);

        // then
        assertTrue(optionalRent.isEmpty());
    }

    @Test
    void shouldCreateNewRent() {
        // given
        Dress dress = new Dress();
        dress.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));
        Integer dressId = dressDao.create(dress);

        Rent rent = new Rent();
        rent.setClient(RandomStringUtils.randomAlphabetic(RENT_CLIENT_SIZE));
        rent.setRentDate(LocalDate.now());
        rent.setDressId(dressId);
        Integer numberOfRecordsBefore = rentDao.findAll().size();

        // when
        Integer rentId = rentDao.create(rent);
        Integer numberOfRecordsAfter = rentDao.findAll().size();

        // then
        assertNotNull(rentId);
        assertEquals(1, numberOfRecordsAfter - numberOfRecordsBefore);
    }

    @Test
    void shouldThrowExceptionWhenCreateNewRentIfDressWasRentedOnThisDay() {
        // given
        Dress dress = new Dress();
        dress.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));
        Integer dressId = dressDao.create(dress);

        Rent rent = new Rent();
        rent.setClient(RandomStringUtils.randomAlphabetic(RENT_CLIENT_SIZE));
        rent.setRentDate(LocalDate.now());
        rent.setDressId(dressId);
        rentDao.create(rent);

        // when -> then
        assertThrows(IllegalArgumentException.class, () -> {
            rentDao.create(rent);
        });
    }

    @Test
    void shouldUpdatedRent() {
        // given
        Dress dress = new Dress();
        dress.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));
        Integer dressId = dressDao.create(dress);

        Rent rent = new Rent();
        rent.setClient(RandomStringUtils.randomAlphabetic(RENT_CLIENT_SIZE));
        rent.setRentDate(LocalDate.now());
        rent.setDressId(dressId);
        Integer rentId = rentDao.create(rent);

        Optional<Rent> optionalRent = rentDao.findById(rentId);
        assertTrue(optionalRent.isPresent());

        optionalRent.get().setClient(RandomStringUtils.randomAlphabetic(RENT_CLIENT_SIZE));

        // when
        int result = rentDao.update(optionalRent.get());

        // then
        assertEquals(1, result);

        Optional<Rent> updatedOptionalRent = rentDao.findById(rentId);
        assertTrue(updatedOptionalRent.isPresent());
        assertEquals(rentId, updatedOptionalRent.get().getRentId());
        assertEquals(optionalRent.get().getClient(), updatedOptionalRent.get().getClient());
    }

    @Test
    void shouldThrowExceptionWhenUpdateRentIfDressWasRentedOnThisDay() {
        // given
        Dress dress = new Dress();
        dress.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));
        Integer dressId = dressDao.create(dress);

        Rent rent = new Rent();
        rent.setClient(RandomStringUtils.randomAlphabetic(RENT_CLIENT_SIZE));
        LocalDate date = LocalDate.of(2019, 1, 1);
        rent.setRentDate(date);
        rent.setDressId(dressId);
        Integer rentId = rentDao.create(rent);
        assertNotNull(rentId);

        Rent anotherRent = new Rent();
        anotherRent.setClient(RandomStringUtils.randomAlphabetic(RENT_CLIENT_SIZE));
        LocalDate anotherDate = LocalDate.of(2020, 1, 1);
        anotherRent.setRentDate(anotherDate);
        anotherRent.setDressId(dressId);
        Integer anotherRentId = rentDao.create(anotherRent);
        assertNotNull(anotherRentId);

        Optional<Rent> optionalAnotherRent = rentDao.findById(anotherRentId);
        assertTrue(optionalAnotherRent.isPresent());
        optionalAnotherRent.get().setRentDate(date);

        // when -> then
        assertThrows(IllegalArgumentException.class, () -> {
            rentDao.update(optionalAnotherRent.get());
        });
    }

    @Test
    void shouldDeleteRent() {
        // given
        Dress dress = new Dress();
        dress.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));
        Integer dressId = dressDao.create(dress);

        Rent rent = new Rent();
        rent.setClient(RandomStringUtils.randomAlphabetic(RENT_CLIENT_SIZE));
        rent.setRentDate(LocalDate.now());
        rent.setDressId(dressId);
        Integer rentId = rentDao.create(rent);
        assertNotNull(rentId);
        Integer numberOfRecordsBefore = rentDao.findAll().size();
        assertNotNull(numberOfRecordsBefore);

        // when
        int result = rentDao.delete(rentId);

        //then
        assertEquals(1, result);
        Integer numberOfRecordsAfter = rentDao.findAll().size();

        assertEquals(1, numberOfRecordsBefore - numberOfRecordsAfter);
    }

    @Test
    void hasDressAlreadyBeenRentedForThisDate() {
        // given
        Dress dress = new Dress();
        dress.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));
        Integer dressId = dressDao.create(dress);

        Rent rent = new Rent();
        rent.setClient(RandomStringUtils.randomAlphabetic(RENT_CLIENT_SIZE));
        LocalDate date = LocalDate.of(2019, 1, 1);
        rent.setRentDate(date);
        rent.setDressId(dressId);
        Integer rentId = rentDao.create(rent);
        assertNotNull(rentId);

        Rent rentWithTheSameData = new Rent();
        rentWithTheSameData.setClient(RandomStringUtils.randomAlphabetic(RENT_CLIENT_SIZE));
        rentWithTheSameData.setRentDate(date);
        rentWithTheSameData.setDressId(dressId);

        Rent anotherRent = new Rent();
        anotherRent.setClient(RandomStringUtils.randomAlphabetic(RENT_CLIENT_SIZE));
        anotherRent.setRentDate(LocalDate.of(2020, 1, 1));
        anotherRent.setDressId(dressId);

        // when -> then
        assertTrue(rentDao.hasDressAlreadyBeenRentedForThisDate(rentWithTheSameData));
        assertFalse(rentDao.hasDressAlreadyBeenRentedForThisDate(anotherRent));
    }
}