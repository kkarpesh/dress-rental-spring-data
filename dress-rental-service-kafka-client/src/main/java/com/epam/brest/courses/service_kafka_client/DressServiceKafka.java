package com.epam.brest.courses.service_kafka_client;

import com.epam.brest.courses.model.dto.DressDto;
import com.epam.brest.courses.service_api.DressService;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DressServiceKafka implements DressService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final NewTopic newTopic;

    public List<String> messages = new ArrayList<>();

    @Autowired
    public DressServiceKafka(KafkaTemplate<String, String> kafkaTemplate,
                             NewTopic newTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.newTopic = newTopic;
    }

    @Override
    public List<DressDto> findAllWithNumberOfOrders() {
        return null;
    }

    @Override
    public Optional<DressDto> findById(Integer dressId) {
        return Optional.empty();
    }

    @Override
    public Integer createOrUpdate(DressDto dressDto) {
        return null;
    }

    @Override
    public Integer delete(Integer dressId) {
        return null;
    }

    @Override
    public Boolean isNameAlreadyExist(DressDto dressDto) {
        return null;
    }

    @Override
    public Boolean isDressHasRents(Integer dressId) {
        return null;
    }

    @KafkaListener(id = "dress", topics = {"dr"})
    public void consume(String message){
        System.out.println("Received Messasge in group dress: " + message);
        messages.add(message);

    }

    public void send (String message){
        ListenableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(newTopic.name(), message);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=["
                        + message + "] due to : " + ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                System.out.println("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
        });
    }


}
