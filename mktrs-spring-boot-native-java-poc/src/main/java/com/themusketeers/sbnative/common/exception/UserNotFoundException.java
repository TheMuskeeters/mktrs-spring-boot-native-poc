/*----------------------------------------------------------------------------*/
/* Source File:   USERNOTFOUNDEXCEPTION.JAVA                                  */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.21/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.common.exception;

/**
 * An exception model for errors for User data.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public class UserNotFoundException extends RuntimeException {

    public static final String USER_WITH_ID = "User with id=[";
    public static final String NOT_FOUND = "] not found";

    /**
     * Constructor with parameter.
     *
     * @param userId Indicates the {@code userId} which was not found.
     */
    public UserNotFoundException(String userId) {
        super(USER_WITH_ID + userId + NOT_FOUND);
    }
}
