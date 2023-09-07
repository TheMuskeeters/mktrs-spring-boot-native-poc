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
    public static final String USER_CONTROLLER_POST_INSERT_USER_INFO = "POST api/v1/users -> Create user.";
    public static final String USER_CONTROLLER_GET_RETRIEVE_USER_INFO = "GET api/v1/users/{userId} -> Retrieve user.";
    public static final String USER_CONTROLLER_PATCH_USER_INFO = "PATCH api/v1/users -> Update user.";
    public static final String USER_CONTROLLER_DELETE_USER_INFO = "DELETE api/v1/users/{userId} -> Remove user.";
    public static final String COLON_SPACE_DELIMITER = ": ";
    public static final String EMPTY_DEFINITION = "\"\"";
    public static final String DOT = ".";
    public static final String SPACE_STR = " ";
    public static final String NULL_STR = "null";
    public static final String KEY_LEFT_BRACKET = "Key [";
    public static final String KEY_MULTIPLE_DEFINED = "] in 'localeData' has more than one value defined.";
    public static final String KEY_RIGHT_BRACKET_SUPPORTED_LIST = "] in 'localeData' is not supported as valid locale key. List of supported language keys are ";
    public static final String KEY_MUST_NOT_CONTAIN_SPACES = "] must not contain spaces.";
    public static final String KEY_CONTAINS_MORE_THAN_ONE_TOKEN = "] contains more than one token.";
    public static final String EN_KEY_MANDATORY = "EN key is mandatory to be present in 'localeData'.";
    public static final String LOCALE_EN = "en";
    public static final String LOCALE_ES = "es";
    public static final String LOCALE_FR = "fr";

    public static final int INT_ZERO = 0;
    public static final int INT_ONE = 1;
    public static final int INT_TWO = 2;

    public static final long LONG_ZERO = 0L;
    public static final long LONG_ONE = 1L;
    public static final long LONG_TWO = 2L;

    /**
     * Utility class, thus no constructor allowed.
     */
    private GlobalConstants() {
    }
}
