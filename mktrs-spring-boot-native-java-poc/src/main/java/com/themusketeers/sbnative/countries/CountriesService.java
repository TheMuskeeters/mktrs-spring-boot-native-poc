/*----------------------------------------------------------------------------*/
/* Source File:   COUNTRIESSERVICE.JAVA                                       */
/* Copyright (c), 2024 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Aug.16/2024  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.countries;

import reactor.core.publisher.Flux;

/**
 * Defines the contract to handle the {@link Countries} in the system.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public interface CountriesService {
    /**
     * Retrieves all {@link Countries} registered
     *
     * @return List of {@link Countries}
     */
    Flux<Countries> findAll();
}
