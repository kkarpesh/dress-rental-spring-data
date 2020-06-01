package com.epam.brest.courses.dao.dto;

import com.epam.brest.courses.dao.config.DaoTestConfig;
import com.epam.brest.courses.model.dto.DressDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = DaoTestConfig.class)
class DressDtoDaoJdbcIT {

    private final DressDtoDao dressDtoDao;

    @Autowired
    DressDtoDaoJdbcIT(DressDtoDao dressDtoDao) {
        this.dressDtoDao = dressDtoDao;
    }

    @Test
    void findAllWithNumberOfOrders() {
        // given
        List<DressDto> dresses = dressDtoDao.findAllWithNumberOfOrders();

        // then
        assertNotNull(dresses);
        assertTrue(dresses.size() > 0);
    }
}