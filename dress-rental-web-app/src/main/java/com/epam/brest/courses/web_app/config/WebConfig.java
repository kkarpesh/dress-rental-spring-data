package com.epam.brest.courses.web_app.config;

import com.epam.brest.courses.service_rest.DressServiceRest;
import com.epam.brest.courses.service_rest.RentServiceRest;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
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

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private NewTopic newTopic;

    @Bean
    public DressServiceRest dressServiceRest() {
        String url = protocol + "://" + host + ":"
                + port + "/" + pointDresses + "/";
        return new DressServiceRest(url, restTemplate(), kafkaTemplate, newTopic);
    }

    @Bean
    public RentServiceRest rentServiceRest() {
        String url = protocol + "://" + host + ":"
                + port + "/" + pointRents + "/";
        return new RentServiceRest(url, restTemplate(), dressServiceRest());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
