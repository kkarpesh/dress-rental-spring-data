package com.epam.brest.courses.rest_app.config;

import com.epam.brest.courses.dao.DressDao;
import com.epam.brest.courses.dao.DressDaoJdbc;
import com.epam.brest.courses.dao.RentDao;
import com.epam.brest.courses.dao.RentDaoJdbc;
import com.epam.brest.courses.dao.dto.DressDtoDao;
import com.epam.brest.courses.dao.dto.DressDtoDaoJdbc;
import com.epam.brest.courses.dao.dto.RentDtoDao;
import com.epam.brest.courses.dao.dto.RentDtoDaoJdbc;
import com.epam.brest.courses.test_db.config.TestDBConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.brest.courses.*")
@PropertySource("classpath:dao.properties")
@Import(TestDBConfig.class)
public class RestConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public DressDao dressDao() {
        return new DressDaoJdbc(namedParameterJdbcTemplate());
    }

    @Bean
    public RentDao rentDao() {
        return new RentDaoJdbc(namedParameterJdbcTemplate());
    }

    @Bean
    public DressDtoDao dressDtoDao() {
        return new DressDtoDaoJdbc(namedParameterJdbcTemplate());
    }

    @Bean
    public RentDtoDao rentDtoDao() {
        return new RentDtoDaoJdbc(namedParameterJdbcTemplate());
    }

}
