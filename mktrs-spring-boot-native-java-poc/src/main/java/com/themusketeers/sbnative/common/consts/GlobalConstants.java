/*----------------------------------------------------------------------------*/
/* Source File:   GLOBALCONSTANTS.JAVA                                        */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 May.30/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.common.consts;

/**
 * General purpose application constants.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public class GlobalConstants {
    public static final String USER_CONTROLLER_GET_RETRIEVE_USERS_INFO = "GET api/v1/users/all -> Retrieving Users";
    public static final String USER_CONTROLLER_POST_INSERT_USER_INFO = "POST api/v1/users -> Retrieving Users";

    public static final int INT_ZERO = 0;
    public static final int INT_ONE = 1;
    public static final int INT_TWO = 2;

    /**
     * Utility class, thus no constructor allowed.
     */
    private GlobalConstants() {
    }
}
