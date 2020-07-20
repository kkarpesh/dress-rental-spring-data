package com.epam.brest.courses.web_app.controller;

import com.epam.brest.courses.model.dto.DressDto;
import com.epam.brest.courses.model.dto.RentDto;
import com.epam.brest.courses.service_api.DressService;
import com.epam.brest.courses.service_api.RentService;
import com.epam.brest.courses.web_app.validators.RentValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Rent controller.
 */
@Controller
@RequestMapping("/rents")
public class RentController {

    /**
     * Default logger for current class.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(RentController.class);

    /**
     * Service layer object to get information of rent.
     */
    private final RentService rentService;


    /**
     * Service layer object to get information of dress.
     */
    private final DressService dressService;

    /**
     * Object to validate rent.
     */
    @Autowired
    private RentValidator rentValidator;

    /**
     * Constructs new object.
     *
     * @param rentService    rentService object.
     * @param dressService   dressService object.
     */
    public RentController(RentService rentService,
                          DressService dressService) {
        this.rentService = rentService;
        this.dressService = dressService;
    }

    /**
     * Goto list of rents by date page.
     *
     * @param dateFrom period start date.
     * @param dateTo   period finish date.
     * @param model    model to storage information for view rendering.
     * @return view name.
     */
    @GetMapping
    public final String getRents(
            @RequestParam(value = "dateFrom", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,
            @RequestParam(value = "dateTo", required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo,
            Model model) {
        LOGGER.debug("Get all rents from {} to {}", dateFrom, dateTo);

        if (dateFrom != null && dateTo != null && dateFrom.isAfter(dateTo)) {
            model.addAttribute("incorrectPeriod", true);
            dateFrom = null;
            dateTo = null;
        }
        List<RentDto> rents =
                rentService.findAllByDate(dateFrom, dateTo);

        model.addAttribute("rents", rents);
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);
        return "rents";
    }

    /**
     * Goto add rent page.
     *
     * @param model model to storage information for view rendering.
     * @return view name.
     */
    @GetMapping("/new")
    public final String gotoAddRentPage(Model model) {
        LOGGER.debug("Goto add rent page {}", model);
        model.addAttribute("isNew", true);
        model.addAttribute("rentDto", new RentDto());
        List<DressDto> dresses = dressService.findAllWithNumberOfOrders();
        model.addAttribute("dresses", dresses);
        return "rent";
    }

    /**
     * Goto edit rent page.
     *
     * @param id    rent ID.
     * @param model model to storage information for view rendering.
     * @return view name.
     */
    @GetMapping("/{id}")
    public final String gotoEditDressPage(@PathVariable Integer id,
                                          Model model) {
        Optional<RentDto> rent = rentService.findById(id);
        if (rent.isPresent()) {
            model.addAttribute("rentDto", rent.get());
            List<DressDto> dresses = dressService.findAllWithNumberOfOrders();
            model.addAttribute("dresses", dresses);
            return "rent";
        } else {
            return "redirect:/rents";
        }
    }

    /**
     * Update or create new rent.
     *
     * @param rentDto rentDto.
     * @param result binding result
     * @param model  to storage information for view rendering.
     * @return view name.
     */
    @PostMapping
    public final String createOrUpdate(@Valid RentDto rentDto,
                                       BindingResult result,
                                       Model model) throws JsonProcessingException {
        rentValidator.validate(rentDto, result);
        List<DressDto> dresses = dressService.findAllWithNumberOfOrders();
        model.addAttribute("dresses", dresses);
        if (rentDto.getRentId() == null) {
            LOGGER.debug("Create new rent {}, {}", rentDto, result);
            if (result.hasErrors()) {
                model.addAttribute("isNew", true);
                return "rent";
            } else {
                rentService.createOrUpdate(rentDto);
                return "redirect:/rents";
            }
        } else {
            LOGGER.debug("Update rent {}, {}", rentDto, result);
            if (result.hasErrors()) {
                return "rent";
            } else {
                rentService.createOrUpdate(rentDto);
                return "redirect:/rents";
            }
        }
    }

    /**
     * Delete rent by ID.
     *
     * @param id rent ID.
     * @return view name.
     */
    @GetMapping("/delete/{id}")
    public final String delete(@PathVariable Integer id) throws JsonProcessingException {
        rentService.delete(id);
        return "redirect:/rents";
    }
}



