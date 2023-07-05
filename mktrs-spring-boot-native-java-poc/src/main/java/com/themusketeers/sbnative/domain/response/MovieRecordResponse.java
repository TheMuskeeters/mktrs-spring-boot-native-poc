/*----------------------------------------------------------------------------*/
/* Source File:   MOVIERECORDRESPONSE.JAVA                                    */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jul.05/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.themusketeers.sbnative.domain.MovieRecord;

/**
 * Holds a movie record from the movie record collection.
 *
 * @param movieRecord Indicates one movie record information retrieved.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"movieRecord"})
public record MovieRecordResponse(MovieRecord movieRecord) {
}
