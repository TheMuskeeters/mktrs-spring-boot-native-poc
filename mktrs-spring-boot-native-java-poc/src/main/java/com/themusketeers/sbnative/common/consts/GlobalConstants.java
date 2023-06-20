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
    public static final String USER_CONTROLLER_GET_RETRIEVE_USERS_INFO = "GET api/v1/users -> Retrieving Users";
    public static final String USER_CONTROLLER_POST_INSERT_USER_INFO = "POST api/v1/users -> Insert user.";
    public static final String USER_CONTROLLER_GET_RETRIEVE_USER_INFO = "GET api/v1/users/{userId}";
    public static final String USER_CONTROLLER_PATCH_USER_INFO = "PATCH api/v1/users -> Insert user.";
    public static final String USER_CONTROLLER_DELETE_USER_INFO = "DELETE api/v1/users/{userId}";

    public static final int INT_ZERO = 0;
    public static final int INT_ONE = 1;
    public static final int INT_TWO = 2;

    /**
     * Utility class, thus no constructor allowed.
     */
    private GlobalConstants() {
    }
}
