package com.epam.brest.courses.web_app.config;

import com.epam.brest.courses.service_rest.DressServiceRest;
import com.epam.brest.courses.service_rest.RentServiceRest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@ComponentScan({"com.epam.brest.courses.*"})
@Configuration
public class WebConfig {

    @Value("${protocol}")
    private String protocol;

    @Value("${host}")
    private String host;

    @Value("${port}")
    private String port;

    @Value("${point.dresses}")
    private String pointDresses;

    @Value("${point.rents}")
    private String pointRents;

    @Bean
    public DressServiceRest dressServiceRest() {
        String url = protocol + "://" + host + ":"
                + port + "/" + pointDresses + "/";
        return new DressServiceRest(url, restTemplate());
    }

    @Bean
    public RentServiceRest rentServiceRest() {
        String url = protocol + "://" + host + ":"
                + port + "/" + pointRents + "/";
        return new RentServiceRest(url, restTemplate());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
