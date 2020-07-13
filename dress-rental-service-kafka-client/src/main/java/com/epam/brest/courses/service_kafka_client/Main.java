package com.epam.brest.courses.service_kafka_client;

import com.epam.brest.courses.model.dto.DressDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@RestController
public class Main {

    int i = 0;

    @Autowired
    DressServiceKafka dressServiceKafka;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping
    public String action() {
        String message = "Kafka " + i++;
        System.out.println(message);
        dressServiceKafka.send(message);
        return message;
    }

    @GetMapping("/hey")
    public String hey() {
        String message = dressServiceKafka.messages.get(dressServiceKafka.messages.size() - 1);
        return message;

    }

}
