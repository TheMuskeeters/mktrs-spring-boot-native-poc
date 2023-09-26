/*----------------------------------------------------------------------------*/
/* Source File:   MARKETINGCARDDOCUMENT.KT                                    */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Sep.26/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.domain.marketing.card.document

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.google.cloud.firestore.annotation.DocumentId
import com.google.cloud.spring.data.firestore.Document
import com.themusketeers.sbnative.domain.marketing.card.MarketingCardCallToAction
import com.themusketeers.sbnative.domain.marketing.card.MarketingCardLocaleData
import com.themusketeers.sbnative.domain.marketing.card.MarketingCardMedia
import com.themusketeers.sbnative.domain.marketing.card.MarketingCardPublishTime

/**
 * Contains the information for Marketing Card (Firestore Document).
 *
 * <p><b>NOTE:</b>As of now. Java Record is not supported in Spring Data Firestore.</p>
 *
 * @param id         Unique identifier for the record.
 * @param actionType Represents the action to be associated with the card.
 * @param timeZone   Indicates the region time associated with the card. For instance, New York.
 * @param sortOrder  Groups cards.
 * @param sites      Indicates a list of locations the card is to be shown.
 * @param publish    Indicates when the marketing card is to be published, start/end time is specified.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Document(collectionName = "marketing-cards-v1")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "title", "description", "actionType", "timeZone", "sortOrder", "publish", "sites", "media", "callToAction", "localeData", "updated")
data class MarketingCardDocument(
    @DocumentId var id: String,
    var title: String,
    var description: String,
    var actionType: String,
    var timeZone: String,
    var updated: String,
    var sortOrder: Int,
    var sites: List<String>,
    var media: MarketingCardMedia,
    var callToAction: MarketingCardCallToAction,
    var localeData: Map<String, MarketingCardLocaleData>,
    var publish: MarketingCardPublishTime,
)