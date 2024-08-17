/*----------------------------------------------------------------------------*/
/* Source File:   CITIESSERVICE.JAVA                                          */
/* Copyright (c), 2024 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Aug.16/2024  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.cities;

import reactor.core.publisher.Flux;

/**
 * Defines the contract to handle the {@link Cities} in the system.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public interface CitiesService {
    /**
     * Retrieves all {@link Cities} registered
     *
     * @return List of {@link Cities}.
     */
    Flux<Cities> findAll();
}
