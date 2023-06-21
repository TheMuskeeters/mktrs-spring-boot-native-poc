/*----------------------------------------------------------------------------*/
/* Source File:   APIERROR.JAVA                                               */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.21/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.common.exception;

import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;

public class ApiError {
    private HttpStatus status;
    private String message;
    private List<String> errors;

    /**
     * Default Constructor
     */
    public ApiError() {
        super();
    }

    /**
     * Constructor with parameters.
     *
     * @param status  Code of the HTTP error.
     * @param message A known description of the error.
     * @param errors  List of known Error descriptions.
     */
    public ApiError(final HttpStatus status, final String message, final List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    /**
     * Constructor with parameters.
     *
     * @param status  Code of the HTTP error.
     * @param message A known description of the error.
     * @param error   Description for a single error.
     */
    public ApiError(final HttpStatus status, final String message, final String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }

    // Getter/Setters

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(final HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(final List<String> errors) {
        this.errors = errors;
    }

    public void setError(final String error) {
        errors = Arrays.asList(error);
    }

}
