/*----------------------------------------------------------------------------*/
/* Source File:   APIEXCEPTION.KT                                             */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.21/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.common.exception

/**
 * An exception model for errors in downstream service requests.
 * @param message   Description of the exception.
 * @param url       Identifies the location where the exception occurred.
 * @param errorCode HTTP error number for the exception.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
class ApiException(message: String, private val url: String, private val errorCode: String) : RuntimeException(message)
