package com.epam.brest.courses.rest_app.config;

import com.epam.brest.courses.test_db.config.TestDBConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan("com.epam.brest.courses.*")
@EnableJpaRepositories(basePackages = "com.epam.brest.courses.*")
@Import(TestDBConfig.class)
public class RestConfig {
}
