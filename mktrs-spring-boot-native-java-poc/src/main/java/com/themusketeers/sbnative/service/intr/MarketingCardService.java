/*----------------------------------------------------------------------------*/
/* Source File:   MARKETINGCARDSERVICE.JAVA                                   */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Sep.26/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.service.intr;

import com.themusketeers.sbnative.domain.marketing.card.MarketingCard;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Registers Marketing Card.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public interface MarketingCardService {

    /**
     * Saves a {@link MarketingCard}.
     *
     * @param marketingCard Info to use for the operation.
     * @return If the {@code id} field is null, tells the repository manager to generate an unique identifier.
     */
    Mono<MarketingCard> save(MarketingCard marketingCard);

    /**
     * Removes a {@link MarketingCard}.
     *
     * @return The information that is removed.
     */
    Mono<MarketingCard> delete(String id);

    /**
     * Update a {@link MarketingCard}.
     *
     * @param id            The unique identifier to be updated.
     * @param marketingCard Information to be updated.
     * @return If the {@code id} field is null, tells the repository manager to generate an unique identifier.
     */
    Mono<MarketingCard> update(String id, MarketingCard marketingCard);

    /**
     * Retrieves all Marketing Cards.
     *
     * @return A stream of {@link MarketingCard}.
     */
    Flux<MarketingCard> retrieveAll();
}
