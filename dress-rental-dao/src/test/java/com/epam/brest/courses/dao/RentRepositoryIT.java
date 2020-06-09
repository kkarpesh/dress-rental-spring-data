package com.epam.brest.courses.dao;

import com.epam.brest.courses.dao.config.DaoTestConfig;
import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.model.Rent;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.epam.brest.courses.constants.DressConstants.DRESS_NAME_SIZE;
import static com.epam.brest.courses.constants.RentConstants.*;
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
    void test(){

    }

}