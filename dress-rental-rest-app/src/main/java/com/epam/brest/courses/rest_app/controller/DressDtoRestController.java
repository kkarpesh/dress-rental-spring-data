package com.epam.brest.courses.rest_app.controller;

import com.epam.brest.courses.model.dto.DressDto;
import com.epam.brest.courses.service_api.dto.DressDtoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Rest controller for work with DressDTOs.
 */
@RestController
@RequestMapping("/dress_dtos")
public class DressDtoRestController {

    /**
     * Default logger for current class.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(DressDtoRestController.class);

    /**
     * Service layer object to get information about Dresses.
     */
    private final DressDtoService dressDtoService;

    /**
     * Constructs new object with given service layer object.
     *
     * @param dressDtoService dressDto service layer object.
     */
    @Autowired
    public DressDtoRestController(DressDtoService dressDtoService) {
        this.dressDtoService = dressDtoService;
    }

    /**
     * Finds all dresses with number of orders.
     * @return dresses with number of orders.
     */
    @GetMapping
    public List<DressDto> findAllWithNumberOfOrders() {
        LOGGER.debug("Find all dresses with number of orders");
        return dressDtoService.findAllWithNumberOfOrders();
    }

}
