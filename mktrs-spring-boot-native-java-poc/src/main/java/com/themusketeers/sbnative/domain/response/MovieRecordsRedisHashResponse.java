/*----------------------------------------------------------------------------*/
/* Source File:   MOVIERECORDSREDISHASHRESPONSE.JAVA                          */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 /2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.themusketeers.sbnative.domain.MovieRecordRedisHash;
import java.util.List;

/**
 * Holds a list of Movie Records.
 *
 * @param count        Indicates how many movie records are registered in the system.
 * @param movieRecords Indicates the movie record list registered in the system.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"count", "movieRecords"})
public record MovieRecordsRedisHashResponse(Long count, List<MovieRecordRedisHash> movieRecords) {
}
