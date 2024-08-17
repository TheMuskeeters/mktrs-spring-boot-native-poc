/*----------------------------------------------------------------------------*/
/* Source File:   CITIESCONTROLLER.JAVA                                       */
/* Copyright (c), 2024 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Aug.16/2024  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.cities.controller.api.v1;

import com.themusketeers.sbnative.cities.Cities;
import com.themusketeers.sbnative.cities.CitiesService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * Cities API Controller.
 * <p><b>Path:</b>{@code api/v1/cities}</p>
 *
 * @param citiesService Instance to access the {@link Cities} Repository
 * @author COQ - Carlos Adolfo Ortiz  Q.
 */
@RestController
@RequestMapping("api/v1/cities")
public record CitiesController(CitiesService citiesService) {
    /**
     * Retrieves a list of registered {@link Cities}.
     * GET /api/v1/cities
     *
     * @return List of registered {@link Cities}.
     */
    public Flux<Cities> findAll() {
        return citiesService.findAll();
    }
}
