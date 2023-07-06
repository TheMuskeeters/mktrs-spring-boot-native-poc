/*----------------------------------------------------------------------------*/
/* Source File:   MOVIERECORDREDISHASH.JAVA                                   */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jul.05/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.redis.core.RedisHash;

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
@RedisHash("MovieRecordRedisHash")
public record MovieRecordRedisHash(@NotEmpty(message = "Movie Record Id is mandatory") String id,
                                   @NotEmpty(message = "Movie Record Title is mandatory") String title,
                                   @NotNull(message = "Movie Record Year is mandatory")
                                   @Min(value = 1900, message = "Movie Record Year must be after 1900")
                                   @Max(value = 9999, message = "Movie Record Year must be before 9999")
                                   Integer year,
                                   @NotEmpty(message = "Movie Record Genre is mandatory") String genre) {
}
