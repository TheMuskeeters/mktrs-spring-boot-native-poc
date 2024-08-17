/*----------------------------------------------------------------------------*/
/* Source File:   COUNTRIESREPOSITORY.JAVA                                    */
/* Copyright (c), 2024 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Aug.16/2024  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.countries;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Defines a contract to retrieve {@link Countries} from the data store.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public interface CountriesRepository extends ReactiveCrudRepository<Countries, String> {
}
