/*----------------------------------------------------------------------------*/
/* Source File:   REGIONSREPOSITORY.JAVA                                      */
/* Copyright (c), 2024 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Aug.14/2024  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.regions;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Defines a contract to retrieve {@link Regions} from the data store.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public interface RegionsRepository extends ReactiveCrudRepository<Regions, String> {
}
