package com.epam.brest.courses.rest_app.controller;

import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.rest_app.exception_handler.ErrorResponse;
import com.epam.brest.courses.service_api.DressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Rest controller for work with Dresses.
 */
@RestController
@RequestMapping("/dresses")
public class DressRestController {

    /**
     * Default logger for current class.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(DressRestController.class);

    /**
     * Short form of dress not found error.
     */
    private static final String DRESS_NOT_FOUND = "dress.not_found";

    /**
     * Service layer object to get information about Dresses.
     */
    private final DressService dressService;

    /**
     * Constructs new object with given service layer object.
     *
     * @param dressService dress service layer object.
     */
    @Autowired
    public DressRestController(DressService dressService) {
        this.dressService = dressService;
    }

    /**
     * Finds all Dresses.
     *
     * @return List with all found Dresses.
     */
    @GetMapping
    public List<Dress> findAll() {
        LOGGER.debug("Find all dresses");
        return dressService.findAll();
    }

    /**
     * Finds Dress with given ID.
     *
     * @param id dress ID.
     * @return Dress.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Dress> findById(@PathVariable Integer id) {
        LOGGER.debug("Find dress by id = {}", id);
        Optional<Dress> dress = dressService.findById(id);
        return dress.isPresent()
                ? new ResponseEntity<>(dress.get(), HttpStatus.OK)
                : new ResponseEntity(
                new ErrorResponse(DRESS_NOT_FOUND,
                        Arrays.asList("Dress not found for id:" + id)),
                HttpStatus.NOT_FOUND);
    }

    /**
     * Creates new dress.
     *
     * @param dress dress.
     * @return created dress ID.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> create(@RequestBody Dress dress) {
        LOGGER.debug("Create new dress {}", dress);
        return new ResponseEntity<>(dressService.create(dress), HttpStatus.OK);
    }

    /**
     * Updates an existing dress with a new object.
     *
     * @param dress dress.
     * @return number of updated records in the database.
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> update(@RequestBody Dress dress) {
        LOGGER.debug("Update dress {}", dress);
        return new ResponseEntity<>(dressService.update(dress), HttpStatus.OK);
    }

    /**
     * Deletes dress from data source.
     *
     * @param id dress.
     * @return number of deleted records in the database.
     */
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> delete(@PathVariable Integer id) {
        LOGGER.debug("Delete dress with id = {}", id);
        return new ResponseEntity<>(dressService.delete(id), HttpStatus.OK);
    }

    /**
     * Checks if the name of the dress is already exist.
     *
     * @param dress dress.
     * @return the boolean value of the existence of a name.
     */
    @PostMapping(value = "/isExists")
    public ResponseEntity<Boolean> isNameAlreadyExists(
            @RequestBody Dress dress) {
        LOGGER.debug("is name exists - {}", dress);
        return new ResponseEntity<>(dressService.isNameAlreadyExist(dress),
                HttpStatus.OK);
    }

    /**
     * Checks if the dress with a given ID has orders.
     *
     * @param id dress ID.
     * @return the boolean value is there a dress orders.
     */
    @GetMapping(value = "/{id}/hasRents")
    public ResponseEntity<Boolean> isDressHasRents(@PathVariable Integer id) {
        LOGGER.debug("is dress id={} has rents", id);
        return new ResponseEntity<>(dressService.isDressHasRents(id),
                HttpStatus.OK);
    }
}
