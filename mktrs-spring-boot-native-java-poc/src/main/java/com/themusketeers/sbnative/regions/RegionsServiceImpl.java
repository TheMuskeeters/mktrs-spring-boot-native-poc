/*----------------------------------------------------------------------------*/
/* Source File:   REGIONSSERVICEIMPL.JAVA                                     */
/* Copyright (c), 2024 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Aug.14/2024  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.regions;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * Handles the {@link Regions} in the system.
 *
 * @param regionsRepository Defines a reference to the instance to manage {@link Regions} from the data store.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Service
public record RegionsServiceImpl(RegionsRepository regionsRepository) implements RegionsService {

    @Override
    public Flux<Regions> findAll() {
        return regionsRepository.findAll();
    }
}
