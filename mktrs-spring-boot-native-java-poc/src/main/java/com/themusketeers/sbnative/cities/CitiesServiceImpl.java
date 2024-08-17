/*----------------------------------------------------------------------------*/
/* Source File:   CITIESSERVICEIMPL.JAVA                                      */
/* Copyright (c), 2024 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Aug.16/2024  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.cities;

import reactor.core.publisher.Flux;

/**
 * Handles the {@link Cities} in the system.
 *
 * @param citiesRepository Defines a reference to the instance to manage {@link Cities} from the data store.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public record CitiesServiceImpl(CitiesRepository citiesRepository) implements CitiesService {

    @Override
    public Flux<Cities> findAll() {
        return citiesRepository.findAll();
    }
}
