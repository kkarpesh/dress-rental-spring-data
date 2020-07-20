package com.epam.brest.courses.service_rest;

import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.model.dto.DressDto;
import com.epam.brest.courses.service_api.DressService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

/**
 * Gets dresses data from RESTful source in JSON format.
 */
public class DressServiceRest implements DressService {

    /**
     * Default logger for current class.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(DressServiceRest.class);

    /**
     * The RESTful source URL.
     */
    private String url;

    /**
     * Synchronous client to perform HTTP request.
     */
    private RestTemplate restTemplate;

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final NewTopic newTopic;

    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Constructs new object with given url and Rest Template object.
     *
     * @param url          url.
     * @param restTemplate res template.
     */
    public DressServiceRest(String url, RestTemplate restTemplate, KafkaTemplate<String, String> kafkaTemplate, NewTopic newTopic) {
        this.url = url;
        this.restTemplate = restTemplate;
        this.kafkaTemplate = kafkaTemplate;
        this.newTopic = newTopic;
    }

    /**
     * Gets all dresses from source.
     *
     * @return dresses list.
     */
    @Override
    public List<DressDto> findAllWithNumberOfOrders() {
        LOGGER.debug("Gets all dresses from REST");
        ResolvableType resolvableType =
                ResolvableType.forClassWithGenerics(List.class, DressDto.class);
        ParameterizedTypeReference<List<DressDto>> refType =
                ParameterizedTypeReference.forType(resolvableType.getType());
        ResponseEntity<List<DressDto>> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, null, refType);
        return responseEntity.getBody();
    }

    /**
     * Gets dress by given ID from source.
     *
     * @param dressId dress Id.
     * @return a Optional description of the dress found.
     */
    @Override
    public Optional<DressDto> findById(Integer dressId) {
        LOGGER.debug("Find dress by ID {}", dressId);
        ResponseEntity<DressDto> responseEntity =
                restTemplate.getForEntity(url + "/" + dressId, DressDto.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    /**
     * Creates new dress.
     *
     * @param dressDto dressDto.
     * @return created dress ID.
     */
    @Override
    public Integer createOrUpdate(DressDto dressDto) throws JsonProcessingException {
        LOGGER.debug("Create new dress {}", dressDto);
        ResponseEntity<Integer> responseEntity =
                restTemplate.postForEntity(url, dressDto, Integer.class);

        sendDresses();

        return responseEntity.getBody();
    }

    /**
     * Deletes dress from data source.
     *
     * @param dressId dress.
     * @return number of deleted records in the database.
     */
    @Override
    public Integer delete(Integer dressId) throws JsonProcessingException {
        LOGGER.debug("Delete dress with id = {}", dressId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Dress> dressHttpEntity = new HttpEntity<>(headers);
        ResponseEntity<Integer> responseEntity =
                restTemplate.exchange(url + "/" + dressId,
                        HttpMethod.DELETE, dressHttpEntity, Integer.class);

        sendDresses();

        return responseEntity.getBody();
    }

    /**
     * Checks if the name of the dress is already exist.
     *
     * @param dressDto dressDto.
     * @return the boolean value of the existence of a name.
     */
    @Override
    public Boolean isNameAlreadyExist(DressDto dressDto) {
        LOGGER.debug("is name exists - {}", dressDto);
        ResponseEntity<Boolean> responseEntity =
                restTemplate.postForEntity(url + "/isExists", dressDto,
                        Boolean.class);
        return responseEntity.getBody();
    }

    /**
     * Checks if the dress with a given ID has orders.
     *
     * @param dressId dress ID.
     * @return the boolean value is there a dress orders.
     */
    @Override
    public Boolean isDressHasRents(Integer dressId) {
        LOGGER.debug("is dress id={} has rents", dressId);
        ResponseEntity<Boolean> responseEntity =
                restTemplate.getForEntity(url + "/" + dressId + "/hasRents",
                        Boolean.class);
        return responseEntity.getBody();
    }

    public void send(String message) {
        LOGGER.debug("send", message);
        ListenableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(newTopic.name(), message);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                LOGGER.debug("Unable to send message=["
                        + message + "] due to : " + ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                LOGGER.debug("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
        });
    }

    public void sendDresses() throws JsonProcessingException {
        List<DressDto> dressDtos = findAllWithNumberOfOrders();
        send(mapper.writeValueAsString(dressDtos));
    }
}
