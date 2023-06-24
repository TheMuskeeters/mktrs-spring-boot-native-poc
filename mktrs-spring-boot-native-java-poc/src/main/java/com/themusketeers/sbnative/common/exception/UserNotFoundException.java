/*----------------------------------------------------------------------------*/
/* Source File:   USERNOTFOUNDEXCEPTION.JAVA                                  */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.21/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.common.exception;

import static com.themusketeers.sbnative.common.consts.ExceptionConstants.NOT_FOUND;
import static com.themusketeers.sbnative.common.consts.ExceptionConstants.USER_WITH_ID;

/**
 * An exception model for errors for User data.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructor with parameter.
     *
     * @param userId Indicates the {@code userId} which was not found.
     */
    public UserNotFoundException(String userId) {
        super(USER_WITH_ID + userId + NOT_FOUND);
    }
}
