/*----------------------------------------------------------------------------*/
/* Source File:   MARKETINGCARDDOCUMENT.KT                                    */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Sep.13/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.domain.document.marketing.card

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.google.cloud.firestore.annotation.DocumentId
import com.google.cloud.spring.data.firestore.Document

/**
 * Contains the information for Marketing Card.
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

/**
 * Contains the information for Marketing Card Call to Action definition.
 *
 * @param label Indicates the text to show for the link (call to action or CTA).
 * @param type  Indicates if it is an internal/external link.
 * @param uri   Denotes the URL to use when clicking the CTA.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("label", "type", "uri")
data class MarketingCardCallToAction(
    var label: String,
    var type: String,
    var uri: String
)

/**
 * Contains the information for Marketing Card Call to Action definition.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("title", "description", "ctaLabel", "media", "callToAction")
data class MarketingCardLocaleData(
    var title: String,
    var description: String,
    var media: MarketingCardMedia,
    var callToAction: MarketingCardCallToAction
)

/**
 * Contains the information for Marketing Card Media definition.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("type", "uri")
data class MarketingCardMedia(
    var type: String? = null,
    var uri: String? = null
)

/**
 * Contains the information received for Marketing Card Publish Time definition.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("start", "end")
class MarketingCardPublishTime(
    var start: String,
    var end: String
)

