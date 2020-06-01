package com.epam.brest.courses.rest_app.controller;

import com.epam.brest.courses.model.dto.RentDto;
import com.epam.brest.courses.service_api.dto.RentDtoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Rest Controller for work with RentDTOs.
 */
@RestController
@RequestMapping("/rent_dtos")
public class RentDtoRestController {
    /**
     * Default logger for current class.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(RentDtoRestController.class);

    /**
     * Service layer object to get information about Rents.
     */
    private final RentDtoService rentDtoService;

    /**
     * Constructs new object with given service layer object.
     *
     * @param rentDtoService dressDto service layer object.
     */
    public RentDtoRestController(RentDtoService rentDtoService) {
        this.rentDtoService = rentDtoService;
    }

    /**
     * Finds rents with dress name for a given period of time.
     *
     * @param dateFrom period start date.
     * @param dateTo   period finish date.
     * @return rents with dress name for a given period of time.
     */
    @GetMapping
    public List<RentDto> findAllWIthDressNameByDate(
            @RequestParam(value = "dateFrom", defaultValue = "2010-01-01")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(value = "dateTo", defaultValue = "2030-01-01")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo) {
        LOGGER.debug("Find all rents with dress name from {} to {}",
                dateFrom,
                dateTo);

        return rentDtoService.findAllWIthDressNameByDate(dateFrom, dateTo);
    }
}
