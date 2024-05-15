/*----------------------------------------------------------------------------*/
/* Source File:   CONTROLLEREXCEPTIONHANDLERCONSTANTS.JAVA                    */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.23/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.common.consts;

/**
 * Constants associated with Controller Exception Handler.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public class ControllerExceptionHandlerConstants {
    /*
     * General
     */
    public static final String USER_NOT_FOUND_ERROR_URL = "/api/v1/users";
    public static final String REST_CLIENT_API_CALL_ISSUE = "Rest Client API call issue.";

    /*
     * Error Category
     */
    public static final String ERROR_CATEGORY_GENERIC = "Generic";
    public static final String ERROR_CATEGORY_PARAMETERS = "Parameters";

    /*
     * Title
     */
    public static final String TITLE_BAD_REQUEST_ON_PAYLOAD = "Bad Request on payload";
    public static final String TITLE_USER_NOT_FOUND = "User Not Found";
    public static final String TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD = "Validation error on supplied payload";

    /*
     * Property
     */
    public static final String PROPERTY_TIMESTAMP = "timestamp";
    public static final String PROPERTY_ERROR_CATEGORY = "errorCategory";
    public static final String PROPERTY_ERRORS = "errors";

    /**
     * Utility class, thus no constructor allowed.
     */
    private ControllerExceptionHandlerConstants() {
    }
}
