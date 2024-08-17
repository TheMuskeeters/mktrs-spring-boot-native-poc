/*----------------------------------------------------------------------------*/
/* Source File:   REGIONSCONTROLLER.JAVA                                      */
/* Copyright (c), 2024 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Aug.14/2024  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.regions.controller.api.v1;

import com.themusketeers.sbnative.regions.Regions;
import com.themusketeers.sbnative.regions.RegionsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * Regions API Controller.
 * <p><b>Path:</b>{@code api/v1/regions}</p>
 *
 * @param regionsService Instance to access the {@link Regions} Repository
 * @author COQ - Carlos Adolfo Ortiz  Q.
 */
@RestController
@RequestMapping("api/v1/regions")
public record RegionsController(RegionsService regionsService) {

    /**
     * Retrieves a list of registered {@link Regions}.
     * GET /api/v1/regions
     *
     * @return List of registered {@link Regions}.
     */
    @GetMapping
    public Flux<Regions> findAll() {
        return regionsService.findAll();
    }
}
