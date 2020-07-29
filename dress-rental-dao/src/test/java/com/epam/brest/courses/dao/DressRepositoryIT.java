package com.epam.brest.courses.dao;

import com.epam.brest.courses.dao.config.DaoTestConfig;
import com.epam.brest.courses.model.Dress;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static com.epam.brest.courses.constants.DressConstants.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DaoTestConfig.class)
class DressRepositoryIT {

    @Autowired
    private DressRepository dressRepository;

    @Test
    void shouldFindByDressName(){
        Dress dress = new Dress();
        String dressName = RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE);
        dress.setDressName(dressName);

        Dress savedDress = dressRepository.save(dress);

        Optional<Dress> foundDress = dressRepository.findByDressName(dressName);
        assertTrue(foundDress.isPresent());
        assertEquals(savedDress.getDressId(), foundDress.get().getDressId());
        assertEquals(savedDress.getDressName(), foundDress.get().getDressName());
    }

    @Test
    void shouldFindByDressNameWhereDressIdNot(){
        Dress dress = new Dress();
        String dressName = RandomStringUtils.randomAlphabetic(DRESS_NAME_SIZE);
        dress.setDressName(dressName);

        Dress savedDress = dressRepository.save(dress);

        Optional<Dress> foundDress = dressRepository.findByDressNameWhereDressIdNot(savedDress.getDressName(), savedDress.getDressId());
        assertTrue(foundDress.isEmpty());
    }

}