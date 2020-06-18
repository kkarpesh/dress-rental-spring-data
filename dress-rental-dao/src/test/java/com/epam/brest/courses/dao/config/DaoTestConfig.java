package com.epam.brest.courses.dao.config;

import com.epam.brest.courses.test_db.config.TestDBConfig;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = {"com.epam.brest.courses.dao"})
@Import(TestDBConfig.class)
public class DaoTestConfig {
}
