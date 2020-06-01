package com.epam.brest.courses.dao.dto;

import com.epam.brest.courses.dao.config.DaoTestConfig;
import com.epam.brest.courses.model.dto.RentDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DaoTestConfig.class)
class RentDtoDaoJdbcIT {

    private final RentDtoDao rentDtoDao;

    @Autowired
    RentDtoDaoJdbcIT(RentDtoDao rentDtoDao) {
        this.rentDtoDao = rentDtoDao;
    }

    private static final LocalDate dateFrom = LocalDate.of(2020, 1, 1);
    private static final LocalDate dateTo = LocalDate.of(2021, 1, 1);


    @Test
    void shouldFindAllWIthDressNameByDate() {
        //when
        List<RentDto> rents = rentDtoDao.findAllWIthDressNameByDate(dateFrom, dateTo);

        // then
        assertNotNull(rents);
        assertTrue(rents.size() > 0);
    }

    @Test
    void shouldThrowsExceptionIfDateFromIsAfterDateTo() {
        assertThrows(IllegalArgumentException.class, () -> {
            rentDtoDao.findAllWIthDressNameByDate(dateTo, dateFrom);
        });
    }
}