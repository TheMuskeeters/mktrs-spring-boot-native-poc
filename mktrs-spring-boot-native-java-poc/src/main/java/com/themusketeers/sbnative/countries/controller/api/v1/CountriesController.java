/*----------------------------------------------------------------------------*/
/* Source File:   COUNTRIESCONTROLLER.JAVA                                    */
/* Copyright (c), 2024 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Aug.16/2024  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.countries.controller.api.v1;

import com.themusketeers.sbnative.countries.Countries;
import com.themusketeers.sbnative.countries.CountriesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * Countries API Controller.
 * <p><b>Path:</b>{@code api/v1/countries}</p>
 *
 * @param countriesService Instance to access the {@link Countries} Repository
 * @author COQ - Carlos Adolfo Ortiz  Q.
 */
@RestController
@RequestMapping("api/v1/countries")
public record CountriesController(CountriesService countriesService) {
    /**
     * Retrieves a list of registered {@link Countries}.
     * GET /api/v1/countries
     *
     * @return List of registered {@link Countries}.
     */
    @GetMapping
    public Flux<Countries> findAll() {
        return countriesService.findAll();
    }
}
