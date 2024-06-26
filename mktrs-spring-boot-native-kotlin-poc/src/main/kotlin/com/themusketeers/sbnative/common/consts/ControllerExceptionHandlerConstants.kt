/*----------------------------------------------------------------------------*/ /* Source File:   CONTROLLEREXCEPTIONHANDLERCONSTANTS.JAVA                    */ /* Copyright (c), 2023 The Musketeers                                         */ /*----------------------------------------------------------------------------*/ /*-----------------------------------------------------------------------------
 History
 Jun.2/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.common.consts

/**
 * Constants associated with Controller Exception Handler.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
object ControllerExceptionHandlerConstants {
    /*
     * General
    */
    const val USER_NOT_FOUND_ERROR_URL = "/api/v1/users"

    /*
     * Error Category
    */
    const val ERROR_CATEGORY_GENERIC = "Generic"
    const val ERROR_CATEGORY_PARAMETERS = "Parameters"

    /*
     * Title
    */
    const val TITLE_BAD_REQUEST_ON_PAYLOAD = "Bad Request on payload"
    const val TITLE_NOT_FOUND = "Not Found"
    const val TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD = "Validation error on supplied payload"

    /*
     * Property
    */
    const val PROPERTY_TIMESTAMP = "timestamp"
    const val PROPERTY_ERROR_CATEGORY = "errorCategory"
    const val PROPERTY_ERRORS = "errors"
}
