/*----------------------------------------------------------------------------*/
/* Source File:   APIEXCEPTION.JAVA                                           */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.21/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.common.exception;

/**
 * An exception model for errors in downstream service requests.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public class ApiException extends RuntimeException {

    private final String url;
    private final String errorCode;

    /**
     * Constructor with parameters.
     *
     * @param message   Description of the exception.
     * @param url       Identifies the location where the exception occurred.
     * @param errorCode HTTP error number for the exception.
     */
    public ApiException(String message, String url, String errorCode) {
        super(message);
        this.url = url;
        this.errorCode = errorCode;
    }
}
