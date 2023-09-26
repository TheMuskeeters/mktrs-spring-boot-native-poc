/*----------------------------------------------------------------------------*/
/* Source File:   CUSTOMCARDREQUEST.JAVA                                      */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Sep.13/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.domain.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.themusketeers.sbnative.domain.marketing.card.MarketingCardLocaleData
import com.themusketeers.sbnative.domain.marketing.card.MarketingCardPublishTime

//private static final String TEXT_PUBLISH_START = "2023-07-17T13:56:00Z";
//private static final String TEXT_PUBLISH_END = "2023-07-31T13:56:00Z";
// StringPublishTime.builder().start(TEXT_PUBLISH_START).end(TEXT_PUBLISH_END).build())

/**
 * Contains the information received for Marketing Card.
 *
 * @param id         Unique identifier for the record.
 * @param actionType Represents the action to be associated with the card.
 * @param timeZone   Indicates the region time associated with the card. For instance, New York.
 * @param sortOrder  Groups cards.
 * @param sites      Indicates a list of locations the card is to be shown.
 * @param publish    Indicates when the marketing card is to be published, start/end time is specified.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class MarketingCardRequest(
    val id: String,
    val actionType: String,
    val timeZone: String,
    val sortOrder: Int,
    val sites: List<String>,
    val localeData: Map<String, MarketingCardLocaleData>,
    val publish: MarketingCardPublishTime
)
