package com.epam.brest.courses.web_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Home controller.
 */
@Controller
public class HomeController {

    /**
     * Redirect to list of dresses page.
     *
     * @return view name.
     */
    @GetMapping(value = "/")
    public final String defaultPageRedirect() {
        return "redirect:/dresses";
    }
}
