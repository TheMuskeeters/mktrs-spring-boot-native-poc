/*----------------------------------------------------------------------------*/
/* Source File:   MARKETINGCARDREPOSITORY.JAVA                                */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Sep.26/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.repository;

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import com.themusketeers.sbnative.domain.marketing.card.document.MarketingCardDocument;
import org.springframework.stereotype.Repository;

/**
 * Spring Data Proxy Interface to store {@link MarketingCardDocument}
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Repository
public interface MarketingCardRepository extends FirestoreReactiveRepository<MarketingCardDocument> {
}
