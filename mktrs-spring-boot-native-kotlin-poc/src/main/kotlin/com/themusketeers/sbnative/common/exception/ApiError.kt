/*----------------------------------------------------------------------------*/
/* Source File:   APIERROR.KT                                                 */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.23/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.common.exception

import org.springframework.http.HttpStatus

class ApiError {
    // Getter/Setters
    lateinit var status: HttpStatus
    lateinit var message: String
    lateinit var errors: List<String>

    /**
     * Default Constructor
     */
    constructor() : super()

    /**
     * Constructor with parameters.
     *
     * @param status  Code of the HTTP error.
     * @param message A known description of the error.
     * @param errors  List of known Error descriptions.
     */
    constructor(status: HttpStatus, message: String, errors: List<String>) : super() {
        this.status = status
        this.message = message
        this.errors = errors
    }

    /**
     * Constructor with parameters.
     *
     * @param status  Code of the HTTP error.
     * @param message A known description of the error.
     * @param error   Description for a single error.
     */
    constructor(status: HttpStatus, message: String, error: String) : super() {
        this.status = status
        this.message = message
        errors = listOf<String>(error)
    }

    fun setError(error: String) {
        errors = listOf(error)
    }
}
