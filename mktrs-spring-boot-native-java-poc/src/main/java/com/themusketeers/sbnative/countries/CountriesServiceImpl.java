/*----------------------------------------------------------------------------*/
/* Source File:   COUNTRIESSERVICEIMPL.JAVA                                   */
/* Copyright (c), 2024 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Aug.16/2024  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.countries;

import reactor.core.publisher.Flux;

/**
 * Handles the {@link Countries} in the system.
 *
 * @param countriesRepository Defines a reference to the instance to manage {@link Countries} from the data store.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public record CountriesServiceImpl(CountriesRepository countriesRepository) implements CountriesService {

    @Override
    public Flux<Countries> findAll() {
        return countriesRepository.findAll();
    }
}
