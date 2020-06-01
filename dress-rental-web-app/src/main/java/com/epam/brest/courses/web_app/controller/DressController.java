package com.epam.brest.courses.web_app.controller;

import com.epam.brest.courses.model.Dress;
import com.epam.brest.courses.model.dto.DressDto;
import com.epam.brest.courses.service_api.DressService;
import com.epam.brest.courses.service_api.dto.DressDtoService;
import com.epam.brest.courses.web_app.validators.DressValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Dress controller.
 */
@Controller
@RequestMapping("/dresses")
public class DressController {

    /**
     * Default logger for current class.
     */
    private static final Logger LOGGER =
            LoggerFactory.getLogger(DressController.class);

    /**
     * Service layer object to get information of dress.
     */
    private final DressService dressService;

    /**
     * Service layer object to get information of dressDto.
     */
    private final DressDtoService dressDtoService;

    /**
     * Object to validate dress.
     */
    @Autowired
    private DressValidator dressValidator;

    /**
     * Constructs new object.
     *
     * @param dressService    dressService object.
     * @param dressDtoService dressDtoService object.
     */
    public DressController(DressService dressService,
                           DressDtoService dressDtoService) {
        this.dressService = dressService;
        this.dressDtoService = dressDtoService;
    }

    /**
     * Goto list of dresses page.
     *
     * @param model model to storage information for view rendering.
     * @return view name.
     */
    @GetMapping
    public final String getAll(Model model) {
        LOGGER.debug("Get all dresses");
        List<DressDto> dresses =
                dressDtoService.findAllWithNumberOfOrders();
        model.addAttribute("dresses", dresses);
        return "dresses";
    }

    /**
     * Goto add dress page.
     *
     * @param model model to storage information for view rendering.
     * @return view name.
     */
    @GetMapping("/new")
    public final String gotoAddDressPage(Model model) {
        LOGGER.debug("Goto add dress page {}", model);
        model.addAttribute("isNew", true);
        model.addAttribute("dress", new Dress());
        return "dress";
    }

    /**
     * Goto edit dress page.
     *
     * @param id    dress ID.
     * @param model model to storage information for view rendering.
     * @return view name.
     */
    @GetMapping("/{id}")
    public final String gotoEditDressPage(@PathVariable Integer id,
                                          Model model) {
        Optional<Dress> dress = dressService.findById(id);
        if (dress.isPresent()) {
            model.addAttribute("dress", dress.get());
            return "dress";
        } else {
            return "redirect:/dresses";
        }
    }

    /**
     * Update or create new dress.
     *
     * @param dress  dress.
     * @param result binding result
     * @param model  to storage information for view rendering.
     * @return view name.
     */
    @PostMapping
    public final String createOrUpdate(@Valid Dress dress,
                                       BindingResult result,
                                       Model model) {
        dressValidator.validate(dress, result);
        if (dress.getDressId() == null) {
            LOGGER.debug("Create new dress {}, {}", dress, result);
            if (result.hasErrors()) {
                model.addAttribute("isNew", true);
                return "dress";
            } else {
                dressService.create(dress);
                return "redirect:/dresses";
            }
        } else {
            LOGGER.debug("Update dress {}, {}", dress, result);
            if (result.hasErrors()) {
                return "dress";
            } else {
                dressService.update(dress);
                return "redirect:/dresses";
            }
        }
    }

    /**
     * Delete dress by ID.
     *
     * @param id    dress ID.
     * @param model to storage information for view rendering.
     * @return view name.
     */
    @GetMapping("/delete/{id}")
    public final String delete(@PathVariable Integer id, Model model) {
        if (dressService.isDressHasRents(id)) {
            model.addAttribute("removalProhibited", true);
            List<DressDto> dresses =
                    dressDtoService.findAllWithNumberOfOrders();
            model.addAttribute("dresses", dresses);
            return "dresses";
        } else {
            dressService.delete(id);
            return "redirect:/dresses";
        }
    }

}
