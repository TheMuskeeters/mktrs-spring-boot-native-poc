/*----------------------------------------------------------------------------*/
/* Source File:   MOVIERECORDREDISHASHRESPONSE.JAVA                           */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jul.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.themusketeers.sbnative.domain.MovieRecordRedisHash;

/**
 * Holds a movie record from the movie record collection.
 *
 * @param movieRecord Indicates one movie record information retrieved.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"movieRecord"})
public record MovieRecordRedisHashResponse(MovieRecordRedisHash movieRecord) {
}
