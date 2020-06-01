package com.epam.brest.courses.rest_app.controller;

import com.epam.brest.courses.model.Rent;
import com.epam.brest.courses.rest_app.exception_handler.ErrorResponse;
import com.epam.brest.courses.service_api.RentService;
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
 * Rest Controller for work with Rents.
 */
@RestController
@RequestMapping("/rents")
public class RentRestController {

    /**
     * Default logger for current class.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(RentRestController.class);

    /**
     * Short form of rent not found error.
     */
    private static final String RENT_NOT_FOUND = "rent.not_found";

    /**
     * Service layer object to get information about Rents.
     */
    private final RentService rentService;

    /**
     * Constructs new object with given service layer object.
     *
     * @param rentService rent service layer object.
     */
    @Autowired
    public RentRestController(RentService rentService) {
        this.rentService = rentService;
    }

    /**
     * Finds all rents.
     *
     * @return rents list.
     */
    @GetMapping
    public List<Rent> findAll() {
        LOGGER.debug("Find all rents");
        return rentService.findAll();
    }

    /**
     * Finds rent by Id.
     *
     * @param id rent Id.
     * @return a Optional description of the rent found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Rent> findById(@PathVariable Integer id) {
        LOGGER.debug("Find rent by id {}", id);
        Optional<Rent> rent = rentService.findById(id);
        return rent.isPresent()
                ? new ResponseEntity<>(rent.get(), HttpStatus.OK)
                : new ResponseEntity(
                new ErrorResponse(RENT_NOT_FOUND,
                        Arrays.asList("Rent not found for id: " + id)),
                HttpStatus.NOT_FOUND);
    }

    /**
     * Creates new rent.
     *
     * @param rent dress.
     * @return created rent Id.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> create(@RequestBody Rent rent) {
        LOGGER.debug("Create new rent {}", rent);
        return new ResponseEntity<>(rentService.create(rent), HttpStatus.OK);
    }

    /**
     * Updates an existing rent with a new object.
     *
     * @param rent rent.
     * @return number of updated records in the database.
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> update(@RequestBody Rent rent) {
        LOGGER.debug("Update rent {}", rent);
        return new ResponseEntity<>(rentService.update(rent), HttpStatus.OK);
    }

    /**
     * Deletes rent from data source.
     *
     * @param id rent Id.
     * @return number of deleted records in the database.
     */
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> delete(@PathVariable Integer id) {
        LOGGER.debug("Delete rent with id = {}", id);
        return new ResponseEntity<>(rentService.delete(id), HttpStatus.OK);
    }

    /**
     * Checks if dress rented for this date.
     *
     * @param rent rent.
     * @return true if dress has already been rented.
     * for this date and false if not.
     */
    @PostMapping(value = "isExists")
    public ResponseEntity<Boolean> isDressRented(@RequestBody Rent rent) {
        LOGGER.debug("is rent exists - {}", rent);
        return new ResponseEntity<>(rentService
                .hasDressAlreadyBeenRentedForThisDate(rent), HttpStatus.OK);
    }

}
