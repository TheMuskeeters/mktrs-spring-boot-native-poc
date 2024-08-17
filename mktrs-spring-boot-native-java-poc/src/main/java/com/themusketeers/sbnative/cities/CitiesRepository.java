/*----------------------------------------------------------------------------*/
/* Source File:   CITIESREPOSITORY.JAVA                                       */
/* Copyright (c), 2024 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Aug.16/2024  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.cities;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Defines a contract to retrieve the {@link Cities} from the data store.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public interface CitiesRepository extends ReactiveCrudRepository<Cities, String> {
}
