package com.epam.brest.courses.service_rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan("com.epam.brest.courses.*")
public class ServiceRestTestConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
