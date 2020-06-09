package com.epam.brest.courses.dao.dto;

import com.epam.brest.courses.model.dto.RentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.LocalDate;
import java.util.List;

/**
 * This class contains methods for direct access
 * to data source using JDBC driver.
 *
 * @author Kirill Karpesh
 * @version 1.0
 * @since 1.0
 */
public class RentDtoDaoJdbc implements RentDtoDao {

    /**
     * Default logger for current class.
     */
    private static final Logger LOGGER
            = LoggerFactory.getLogger(RentDtoDaoJdbc.class);

    /**
     * The database query to find all rents with dress name
     * for a given period of time.
     */
    @Value("${rentDto.findAllWIthDressNameByDate}")
    private String findAllWIthDressNameSql;

    /**
     * Jdbc template to execute actions to data source.
     */
    private final NamedParameterJdbcTemplate jdbcTemplate;

    /**
     * Constructs a new object with given JDBC template.
     *
     * @param jdbcTemplate jdbc template.
     */
    public RentDtoDaoJdbc(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Finds rents with dress name for a given period of time.
     *
     * @return rents with dress name for a given period of time.
     */
    @Override
    public List<RentDto> findAllWIthDressNameByDate(LocalDate dateFrom,
                                                    LocalDate dateTo) {
        LOGGER.debug("Find all rents with dress name from {} to {}",
                dateFrom, dateTo);
        if (dateFrom.isAfter(dateTo)) {
            throw new IllegalArgumentException(
                    "'Date from' cannot be after 'date to'."
            );
        }

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("dateFrom", dateFrom);
        namedParameters.addValue("dateTo", dateTo);
        return jdbcTemplate
                .query(findAllWIthDressNameSql,
                        namedParameters,
                        BeanPropertyRowMapper.newInstance(RentDto.class));
    }
}
