/*----------------------------------------------------------------------------*/
/* Source File:   GLOBALCONTROLLEREXCEPTIONHANDLER.JAVA                       */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 May.17/2024  COQ  File created.
 May.30/2024  COQ  Change implementation for handlers.
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
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

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
     * @param ex       Instance to the whole problem.
     * @param exchange Instance with information about the request.
     * @return A message indicating properly when this exception is raised that the system has not properly managed.
     * @see RuntimeException
     * @see UserNotFoundException
     * @see ResponseEntity
     * @see ProblemDetail
     */
    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundExceptionX(ex: java.lang.RuntimeException, exchange: ServerWebExchange): Mono<ResponseEntity<Any>> {
        val httpStatus = HttpStatus.NOT_FOUND
        val problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, ex.message)
        val instanceURL = exchange.request.uri.path
        val headers = exchange.request.headers

        problemDetail.type = URI.create(instanceURL)
        problemDetail.instance = URI.create(instanceURL)
        problemDetail.setProperty(PROPERTY_ERROR_CATEGORY, ERROR_CATEGORY_GENERIC)
        problemDetail.setProperty(PROPERTY_TIMESTAMP, Instant.now())

        return this.createResponseEntity(problemDetail, headers, httpStatus, exchange)
    }

    override fun handleWebExchangeBindException(
        ex: WebExchangeBindException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        exchange: ServerWebExchange
    ): Mono<ResponseEntity<Any>> {
        val instanceURL = exchange.request.uri.path
        val problemDetail = ProblemDetail.forStatusAndDetail(status!!, TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)

        problemDetail.title = TITLE_BAD_REQUEST_ON_PAYLOAD
        problemDetail.type = URI.create(instanceURL)
        problemDetail.instance = URI.create(instanceURL)
        problemDetail.setProperty(PROPERTY_TIMESTAMP, Instant.now())
        problemDetail.setProperty(PROPERTY_ERROR_CATEGORY, ERROR_CATEGORY_PARAMETERS)
        problemDetail.setProperty(
            PROPERTY_ERRORS, Stream.concat<String>(
                ex.bindingResult
                    .fieldErrors
                    .stream()
                    .map<String> { field: FieldError -> field.field + COLON_SPACE_DELIMITER + field.defaultMessage },
                ex.bindingResult
                    .globalErrors
                    .stream()
                    .map<String> { field1: ObjectError -> field1.objectName + COLON_SPACE_DELIMITER + field1.defaultMessage })
                .sorted()
                .toList()
        )

        return this.createResponseEntity(problemDetail, headers, status, exchange)
    }
}
