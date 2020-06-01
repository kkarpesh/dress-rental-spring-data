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

import static org.junit.jupiter.api.Assertions.*;
import static com.epam.brest.courses.constants.DressConstants.*;
import static com.epam.brest.courses.constants.RentConstants.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DaoTestConfig.class)
class DressDaoJdbcIT {

    private final DressDao dressDao;
    private final RentDao rentDao;

    @Autowired
    DressDaoJdbcIT(DressDao dressDao, RentDao rentDao) {
        this.dressDao = dressDao;
        this.rentDao = rentDao;
    }

    @Test
    void shouldFindAllDresses() {
        // when
        List<Dress> dresses = dressDao.findAll();

        //then
        assertNotNull(dresses);
        assertTrue(dresses.size() > 0);
    }

    @Test
    void shouldFindDressWithExistingId() {
        // given
        Dress dress = new Dress();
        dress.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));
        Integer dressId = dressDao.create(dress);

        // when
        Optional<Dress> optionalDress = dressDao.findById(dressId);

        // then
        assertTrue(optionalDress.isPresent());
        assertEquals(dress.getDressName(), optionalDress.get().getDressName());
    }

    @Test
    void shouldReturnNullWhenFindByNonexistentId() {
        // given
        Dress dress = new Dress();
        dress.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));
        Integer dressId = dressDao.create(dress);
        dressDao.delete(dressId);

        // when
        Optional<Dress> optionalDress = dressDao.findById(dressId);

        // then
        assertTrue(optionalDress.isEmpty());
    }

    @Test
    void shouldCreateNewDress() {
        // given
        Dress dress = new Dress();
        dress.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));
        Integer numberOfRecordsBefore = dressDao.findAll().size();

        // when
        Integer dressId = dressDao.create(dress);
        Integer numberOfRecordsAfter = dressDao.findAll().size();

        //then
        assertNotNull(dressId);
        assertEquals(1, (numberOfRecordsAfter - numberOfRecordsBefore));
    }

    @Test
    void shouldThrowExceptionWhenCreateNewDressWithExistingName() {
        // given
        Dress dress = new Dress();
        dress.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));
        dressDao.create(dress);

        // when -> then
        assertThrows(IllegalArgumentException.class, () -> {
            dressDao.create(dress);
        });
    }

    @Test
    void shouldUpdatedDress() {
        // given
        Dress dress = new Dress();
        dress.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));
        Integer dressId = dressDao.create(dress);

        Optional<Dress> optionalDress = dressDao.findById(dressId);
        assertTrue(optionalDress.isPresent());

        optionalDress.get().setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));

        // when
        int result = dressDao.update(optionalDress.get());

        // then
        assertEquals(1, result);

        Optional<Dress> updatedOptionalDress = dressDao.findById(dressId);
        assertTrue(updatedOptionalDress.isPresent());
        assertEquals(dressId, updatedOptionalDress.get().getDressId());
        assertEquals(optionalDress.get().getDressName(), updatedOptionalDress.get().getDressName());
    }

    @Test
    void shouldThrowExceptionWhenUpdateDressWithExistingName() {
        // given
        Dress dress = new Dress();
        String dressName = RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE);
        dress.setDressName(dressName);
        Integer dressId = dressDao.create(dress);
        assertNotNull(dressId);

        Dress anotherDress = new Dress();
        anotherDress.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));
        Integer anotherDressId = dressDao.create(anotherDress);
        assertNotNull(anotherDressId);

        Optional<Dress> optionalAnotherDress = dressDao.findById(anotherDressId);
        assertTrue(optionalAnotherDress.isPresent());
        optionalAnotherDress.get().setDressName(dressName);

        // when -> then
        assertThrows(IllegalArgumentException.class, () -> {
            dressDao.update(optionalAnotherDress.get());
        });
    }

    @Test
    void shouldDeleteDressThatDoesNotHaveRents() {
        // given
        Dress dress = new Dress();
        dress.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));
        Integer dressId = dressDao.create(dress);
        assertNotNull(dressId);
        Integer numberOfRecordsBefore = dressDao.findAll().size();
        assertNotNull(numberOfRecordsBefore);

        // when
        int result = dressDao.delete(dressId);

        // then
        assertEquals(1, result);
        Integer numberOfRecordsAfter = dressDao.findAll().size();
        assertNotNull(numberOfRecordsAfter);

        assertEquals(1, (numberOfRecordsBefore - numberOfRecordsAfter));
    }

    @Test
    void shouldThrowExceptionWhenDeleteDressWithRents() {
        // given
        Dress dress = new Dress();
        dress.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));
        Integer dressId = dressDao.create(dress);
        assertNotNull(dressId);

        Rent rent = new Rent();
        rent.setClient(RandomStringUtils.randomAlphabetic(RENT_CLIENT_SIZE));
        rent.setRentDate(LocalDate.now());
        rent.setDressId(dressId);
        Integer rentId = rentDao.create(rent);
        assertNotNull(rentId);

        // when -> then
        assertThrows(UnsupportedOperationException.class, () -> {
            dressDao.delete(dressId);
        });
    }

    @Test
    void isNameAlreadyExist() {
        // given
        Dress dress = new Dress();
        String dressName = RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE);
        dress.setDressName(dressName);
        Integer dressId = dressDao.create(dress);
        assertNotNull(dressId);

        Dress dressWithTheSameName = new Dress();
        dressWithTheSameName.setDressName(dressName);

        Dress anotherDress = new Dress();
        dress.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));

        // when -> then
        assertTrue(dressDao.isNameAlreadyExist(dressWithTheSameName));
        assertFalse(dressDao.isNameAlreadyExist(anotherDress));
    }

    @Test
    void isDressHasRents() {
        // given
        Dress dressWithRents = new Dress();
        dressWithRents.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));
        Integer dressWithRentsId = dressDao.create(dressWithRents);
        assertNotNull(dressWithRentsId);

        Rent rent = new Rent();
        rent.setClient(RandomStringUtils.randomAlphabetic(RENT_CLIENT_SIZE));
        rent.setRentDate(LocalDate.now());
        rent.setDressId(dressWithRentsId);
        Integer rentId = rentDao.create(rent);
        assertNotNull(rentId);

        Dress dressWithoutRents = new Dress();
        dressWithoutRents.setDressName(RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE));
        Integer dressWithoutRentsId = dressDao.create(dressWithoutRents);
        assertNotNull(dressWithoutRentsId);

        // when -> then
        assertTrue(dressDao.isDressHasRents(dressWithRentsId));
        assertFalse(dressDao.isDressHasRents(dressWithoutRentsId));
    }
}