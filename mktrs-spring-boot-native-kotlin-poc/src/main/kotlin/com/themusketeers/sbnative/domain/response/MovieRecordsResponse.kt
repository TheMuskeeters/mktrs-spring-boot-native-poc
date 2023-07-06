/*----------------------------------------------------------------------------*/
/* Source File:   MOVIERECORDSRESPONSE.KT                                     */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jul.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.domain.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.themusketeers.sbnative.domain.MovieRecord
import com.themusketeers.sbnative.domain.MovieRecordRedisHash

/**
 * Holds a movie record from the movie record collection.
 *
 * @param movieRecord Indicates one movie record information retrieved.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("movieRecord")
data class MovieRecordRedisHashResponse(val movieRecord: MovieRecordRedisHash)

/**
 * Holds a list of Movie Records.
 *
 * @param count        Indicates how many movie records are registered in the system.
 * @param movieRecords Indicates the movie record list registered in the system.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("count", "movieRecords")
data class MovieRecordsRedisHashResponse(val count: Long, val movieRecords: List<MovieRecordRedisHash>)

/**
 * Holds a movie record from the movie record collection.
 *
 * @param movieRecord Indicates one movie record information retrieved.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("movieRecord")
data class MovieRecordResponse(val movieRecord: MovieRecord)

/**
 * Holds a list of Movie Records.
 *
 * @param count        Indicates how many movie records are registered in the system.
 * @param movieRecords Indicates the movie record list registered in the system.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("count", "movieRecords")
data class MovieRecordsResponse(val count: Long, val movieRecords: List<MovieRecord>)