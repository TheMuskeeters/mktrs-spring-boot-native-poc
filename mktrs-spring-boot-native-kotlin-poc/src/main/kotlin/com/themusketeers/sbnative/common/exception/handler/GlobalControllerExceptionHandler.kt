/*----------------------------------------------------------------------------*/
/* Source File:   GLOBALCONTROLLEREXCEPTIONHANDLER.JAVA                       */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.24/2023  COQ  File created.
 May.29/2024  COQ  Change implementation for handlers.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.common.exception.handler

import com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.ERROR_CATEGORY_GENERIC
import com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.ERROR_CATEGORY_PARAMETERS
import com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.PROPERTY_ERRORS
import com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.PROPERTY_ERROR_CATEGORY
import com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.PROPERTY_TIMESTAMP
import com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.TITLE_BAD_REQUEST_ON_PAYLOAD
import com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD
import com.themusketeers.sbnative.common.consts.GlobalConstants.COLON_SPACE_DELIMITER
import com.themusketeers.sbnative.common.exception.ApiException
import com.themusketeers.sbnative.common.exception.UserNotFoundException
import java.net.URI
import java.time.Instant
import java.util.stream.Stream
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * Put in a global place the exception handling mechanism, this is shared among all the REST Controllers defined
 * in the application. Annotate a handler with the {@link ExceptionHandler} annotation to indicate which error
 * message should return as the response when it is raised.
 *
 * <p>Check some useful reference links
 * <ul>
 * <li><a href="https://datatracker.ietf.org/doc/html/rfc9457">RFC 9457: Problem Details for HTTP APIs</a></li>
 * <li><a href="https://www.baeldung.com/global-error-handler-in-a-spring-rest-api">Global Error Handler in A Spring Rest Api</a></li>
 * <li><a href="https://www.youtube.com/watch?v=4YyJUS_7rQE">Spring 6 and Problem Details</a></li>
 * </ul>
 * </p>
 *
 * @author COQ - Carlos Adolfo Ortiz Quirós
 */
@RestControllerAdvice
class GlobalControllerExceptionHandler : ResponseEntityExceptionHandler() {
    /**
     * Defines the message to be returned as the response when the {@link ApiException} is raised.
     * Contains the information of the thrown exception to include as part of the response.
     *
     * @param ex Instance to the whole problem.
     * @return A message indicating properly when this exception is raised that the system has not properly managed.
     * @see RuntimeException
     */
    @ExceptionHandler(ApiException::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleInternalError(ex: RuntimeException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    /**
     * Reports as response when the exception is raised indicating an User was not found.
     *
     * @param ex      Instance to the whole problem.
     * @param request Instance with information about the request.
     * @return A message indicating properly when this exception is raised that the system has not properly managed.
     * @see RuntimeException
     * @see UserNotFoundException
     * @see ResponseEntity
     * @see ProblemDetail
     */
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundExceptionX(ex: java.lang.RuntimeException, request: WebRequest): ResponseEntity<Any> {
        val httpStatus = HttpStatus.NOT_FOUND
        val problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, ex.message)
        val instanceURL = (request as ServletWebRequest).request.requestURI // This cast is for Servlet use case.

        problemDetail.type = URI.create(instanceURL)
        problemDetail.instance = URI.create(instanceURL)
        problemDetail.setProperty(PROPERTY_ERROR_CATEGORY, ERROR_CATEGORY_GENERIC)
        problemDetail.setProperty(PROPERTY_TIMESTAMP, Instant.now())

        // Pending how to extract all headers to comply with HttpHeaders class. May.28/2024
        val httpHeaders = HttpHeaders()
        return this.createResponseEntity(problemDetail, httpHeaders, httpStatus, request)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        val instanceURL = (request as ServletWebRequest).request.requestURI // This cast is for Servlet use case.
        val problemDetail = ProblemDetail.forStatusAndDetail(status, TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)

        problemDetail.title = TITLE_BAD_REQUEST_ON_PAYLOAD
        problemDetail.type = URI.create(instanceURL)
        problemDetail.instance = URI.create(instanceURL)
        problemDetail.setProperty(PROPERTY_TIMESTAMP, Instant.now())
        problemDetail.setProperty(PROPERTY_ERROR_CATEGORY, ERROR_CATEGORY_PARAMETERS)
        problemDetail.setProperty(
            PROPERTY_ERRORS, Stream.concat(
                ex.bindingResult
                    .fieldErrors
                    .stream()
                    .map { field: FieldError -> field.field + COLON_SPACE_DELIMITER + field.defaultMessage },
                ex.bindingResult
                    .globalErrors
                    .stream()
                    .map { field1: ObjectError -> field1.objectName + COLON_SPACE_DELIMITER + field1.defaultMessage })
                .sorted()
                .toList()
        )

        return this.createResponseEntity(problemDetail, headers, status, request)
    }
}
