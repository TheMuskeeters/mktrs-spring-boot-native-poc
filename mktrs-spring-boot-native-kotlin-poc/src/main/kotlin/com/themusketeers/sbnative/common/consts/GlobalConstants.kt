/*----------------------------------------------------------------------------*/
/* Source File:   GLOBALCONSTANTS.KT                                          */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 May.30/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.common.consts

/**
 * General purpose application constants.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
object GlobalConstants {
    const val USER_CONTROLLER_GET_RETRIEVE_USERS_INFO = "GET api/v1/users -> Retrieving Users"
    const val USER_CONTROLLER_POST_INSERT_USER_INFO = "POST api/v1/users -> Create user."
    const val USER_CONTROLLER_GET_RETRIEVE_USER_INFO = "GET api/v1/users/{userId} -> Retrieve user."
    const val USER_CONTROLLER_PATCH_USER_INFO = "PATCH api/v1/users -> Update user."
    const val USER_CONTROLLER_DELETE_USER_INFO = "DELETE api/v1/users/{userId} -> Remove user."
    const val COLON_SPACE_DELIMITER = ": "
    const val EMPTY_DEFINITION = "\"\""
    const val DOT = "."
    const val WILD_CARD_ASTERISK = "*"
    const val EMPTY_STRING = ""
    const val NULL_STRING = "null"
    const val SPACE_STR = " "
    const val NULL_STR = "null"
    const val KEY_LEFT_BRACKET = "Key ["
    const val KEY_MULTIPLE_DEFINED = "] in 'localeData' has more than one value defined."
    const val KEY_RIGHT_BRACKET_SUPPORTED_LIST = "] in 'localeData' is not supported as valid locale key. List of supported language keys are "
    const val KEY_MUST_NOT_CONTAIN_SPACES = "] must not contain spaces."
    const val KEY_CONTAINS_MORE_THAN_ONE_TOKEN = "] contains more than one token."
    const val EN_KEY_MANDATORY = "EN key is mandatory to be present in 'localeData'."
    const val LOCALE_EN = "en"
    const val LOCALE_ES = "es"
    const val LOCALE_FR = "fr"

    const val INT_ZERO = 0
    const val INT_ONE = 1
    const val INT_TWO = 2
    const val LONG_ZERO = 0L
    const val LONG_ONE = 1L
    const val LONG_TWO = 2L
}
