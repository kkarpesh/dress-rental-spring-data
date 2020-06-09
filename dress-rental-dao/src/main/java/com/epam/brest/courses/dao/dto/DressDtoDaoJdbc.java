package com.epam.brest.courses.dao.dto;

import com.epam.brest.courses.model.dto.DressDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

/**
 * This class contains methods for direct access
 * to data source using JDBC driver.
 *
 * @author Kirill Karpesh
 * @version 1.0
 * @since 1.0
 */
public class DressDtoDaoJdbc implements DressDtoDao {

    /**
     * Default logger for current class.
     */
    private static final Logger LOGGER
            = LoggerFactory.getLogger(DressDtoDaoJdbc.class);

    /**
     * The database query to find all dresses
     * with number of orders.
     */
    @Value("${dressDto.findAllWithNumberOfOrders}")
    private String findAllWithNumberOfOrdersSql;

    /**
     * Jdbc template to execute actions to data source.
     */
    private final NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * Constructs a new object with given JDBC template.
     *
     * @param jdbcTemplate jdbc template.
     */
    public DressDtoDaoJdbc(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Finds dresses with number of orders.
     *
     * @return dresses with number of orders.
     */
    @Override
    public List<DressDto> findAllWithNumberOfOrders() {
        LOGGER.debug("Find all dresses with number of orders");
        return jdbcTemplate
                .query(findAllWithNumberOfOrdersSql,
                        BeanPropertyRowMapper.newInstance(DressDto.class));
    }
}
