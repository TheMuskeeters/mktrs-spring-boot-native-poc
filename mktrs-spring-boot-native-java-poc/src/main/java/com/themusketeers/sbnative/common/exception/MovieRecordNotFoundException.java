/*----------------------------------------------------------------------------*/
/* Source File:   MOVIERECORDNOTFOUNDEXCEPTION.JAVA                           */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jul.05/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.common.exception;

import static com.themusketeers.sbnative.common.consts.ExceptionConstants.MOVIE_RECORD_WITH_ID;
import static com.themusketeers.sbnative.common.consts.ExceptionConstants.NOT_FOUND;

/**
 * An exception model for errors for Movie Record data.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public class MovieRecordNotFoundException extends RuntimeException {
    /**
     * Constructor with parameter.
     *
     * @param movieId Indicates the {@code movieId} which was not found.
     */
    public MovieRecordNotFoundException(String movieId) {
        super(MOVIE_RECORD_WITH_ID+movieId+ NOT_FOUND);
    }
}
