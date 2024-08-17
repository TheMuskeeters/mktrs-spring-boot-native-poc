/*----------------------------------------------------------------------------*/
/* Source File:   REGIONSSERVICE.JAVA                                         */
/* Copyright (c), 2024 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Aug.14/2024  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.regions;

import reactor.core.publisher.Flux;

/**
 * Defines the contract to handle the {@link Regions} in the system.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public interface RegionsService {

    /**
     * Retrieves all {@link Regions} registered
     *
     * @return List of {@link Regions}.
     */
    Flux<Regions> findAll();
}
