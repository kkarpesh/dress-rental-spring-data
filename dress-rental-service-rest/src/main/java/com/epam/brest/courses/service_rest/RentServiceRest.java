package com.epam.brest.courses.service_rest;

import com.epam.brest.courses.model.Rent;
import com.epam.brest.courses.model.dto.RentDto;
import com.epam.brest.courses.service_api.RentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Gets rents data from RESTful source in JSON format.
 */
public class RentServiceRest implements RentService {

    /**
     * Default logger for current class.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(RentServiceRest.class);

    /**
     * The RESTful source URL.
     */
    private String url;

    /**
     * Synchronous client to perform HTTP request.
     */
    private RestTemplate restTemplate;

    /**
     * Constructs new object with given url and Rest Template object.
     *
     * @param url          url.
     * @param restTemplate res template.
     */
    public RentServiceRest(String url, RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    /**
     * Gets rents with dress name for a given period of time.
     *
     * @param dateFrom period start date.
     * @param dateTo   period finish date.
     * @return rents with dress name for a given period of time.
     */
    @Override
    public List<RentDto> findAllByDate(LocalDate dateFrom,
                                       LocalDate dateTo) {
        LOGGER.debug("Gets all dresses from REST");

        String fullUrl = url;

        if (dateFrom != null && dateTo == null) {
            fullUrl += "?dateFrom=" + dateFrom.toString();
        }
        if (dateFrom == null && dateTo != null) {
            fullUrl += "?dateTo=" + dateTo.toString();
        }
        if (dateFrom != null && dateTo != null) {
            fullUrl += "?dateFrom=" + dateFrom.toString()
                    + "&dateTo=" + dateTo.toString();
        }

        ResolvableType resolvableType =
                ResolvableType.forClassWithGenerics(List.class, RentDto.class);
        ParameterizedTypeReference<List<RentDto>> refType =
                ParameterizedTypeReference.forType(resolvableType.getType());
        ResponseEntity<List<RentDto>> responseEntity =
                restTemplate.exchange(fullUrl, HttpMethod.GET,
                        null, refType);
        return responseEntity.getBody();
    }

    /**
     * Gets rent by given ID from source.
     *
     * @param rentId rent Id.
     * @return a Optional description of the rent found.
     */
    @Override
    public Optional<RentDto> findById(Integer rentId) {
        LOGGER.debug("Find dress by ID {}", rentId);
        ResponseEntity<RentDto> responseEntity =
                restTemplate.getForEntity(url + "/" + rentId, RentDto.class);
        return Optional.ofNullable(responseEntity.getBody());
    }

    /**
     * Creates new rent.
     *
     * @param rentDto rentDto.
     * @return created rent ID.
     */
    @Override
    public Integer createOrUpdate(RentDto rentDto) {
        LOGGER.debug("Create new rent {}", rentDto);
        ResponseEntity<Integer> responseEntity =
                restTemplate.postForEntity(url, rentDto, Integer.class);
        return responseEntity.getBody();
    }

    /**
     * Deletes rent from data source.
     *
     * @param rentId rent.
     * @return number of deleted records in the database.
     */
    @Override
    public Integer delete(Integer rentId) {
        LOGGER.debug("Delete rent with id = {}", rentId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Rent> rentHttpEntity = new HttpEntity<>(headers);
        ResponseEntity<Integer> responseEntity =
                restTemplate.exchange(url + "/" + rentId,
                        HttpMethod.DELETE, rentHttpEntity, Integer.class);
        return responseEntity.getBody();
    }

    /**
     * Checks if dress rented for this date.
     *
     * @param rentDto rentDto.
     * @return true if dress has already been rented
     * for this date and false if not.
     */
    @Override
    public Boolean isDressRented(RentDto rentDto) {
        LOGGER.debug("is rent exists - {}", rentDto);
        ResponseEntity<Boolean> responseEntity =
                restTemplate.postForEntity(url + "/isExists",
                        rentDto, Boolean.class);
        return responseEntity.getBody();
    }
}
