/*----------------------------------------------------------------------------*/
/* Source File:   MOVIERECORDNOTFOUNDEXCEPTION.KT                             */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jul.05/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.common.exception

import com.themusketeers.sbnative.common.consts.ExceptionConstants.MOVIE_RECORD_WITH_ID
import com.themusketeers.sbnative.common.consts.ExceptionConstants.NOT_FOUND

/**
 * An exception model for errors for Movie Record data.
 *
 * @param movieId Indicates the `movieId` which was not found.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
class MovieRecordNotFoundException(movieId: String) : RuntimeException(MOVIE_RECORD_WITH_ID + movieId + NOT_FOUND)
