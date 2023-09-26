/*----------------------------------------------------------------------------*/
/* Source File:   CUSTOMCARDREQUEST.JAVA                                      */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Sep.05/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.domain.request;

//private static final String TEXT_PUBLISH_START = "2023-07-17T13:56:00Z";
//private static final String TEXT_PUBLISH_END = "2023-07-31T13:56:00Z";
// StringPublishTime.builder().start(TEXT_PUBLISH_START).end(TEXT_PUBLISH_END).build())

import com.fasterxml.jackson.annotation.JsonInclude;
import com.themusketeers.sbnative.domain.document.marketing.card.MarketingCardLocaleData;
import com.themusketeers.sbnative.domain.document.marketing.card.MarketingCardPublishTime;
import java.util.List;
import java.util.Map;

/**
 * Contains the information received for Marketing Card.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record MarketingCardRequest(String id,
                                   String actionType,
                                   String timeZone,
                                   Integer sortOrder,
                                   List<String> sites,
                                   Map<String, MarketingCardLocaleData> localeData,
                                   MarketingCardPublishTime publish) {
}
