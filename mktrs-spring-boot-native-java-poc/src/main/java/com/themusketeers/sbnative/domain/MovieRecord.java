/*----------------------------------------------------------------------------*/
/* Source File:   MOVIERECORD.JAVA                                            */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jul.05/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotEmpty;

/**
 * Represents Movie Record information.
 *
 * @param id    Identifies the Movie Record.
 * @param title Indicates the name of the Movie Record (mandatory)
 * @param year  Indicates the inception year of the Movie Record (mandatory).
 * @param genre Indicates the classification given (mandatory).
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "title", "year", "genre"})
public record MovieRecord(@NotEmpty(message = "Movie Record Id is mandatory") String id,
                          @NotEmpty(message = "Movie Record Title is mandatory") String title,
                          @NotEmpty(message = "Movie Record Year is mandatory") int year,
                          @NotEmpty(message = "Movie Record Genre is mandatory") String genre) {
}
