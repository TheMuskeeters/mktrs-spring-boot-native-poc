/*----------------------------------------------------------------------------*/
/* Source File:   GLOBALCONTROLLEREXCEPTIONHANDLER.JAVA                       */
/* Copyright (c), 2024 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 May.14/2024  COQ  File created.
 May.30/2024  COQ  Change implementation for handlers.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.common.exception.handler;

import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.ERROR_CATEGORY_GENERIC;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.ERROR_CATEGORY_PARAMETERS;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.PROPERTY_ERRORS;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.PROPERTY_ERROR_CATEGORY;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.PROPERTY_TIMESTAMP;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.REST_CLIENT_API_CALL_ISSUE;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.TITLE_BAD_REQUEST_ON_PAYLOAD;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.COLON_SPACE_DELIMITER;

import com.themusketeers.sbnative.common.exception.ApiException;
import com.themusketeers.sbnative.common.exception.UserNotFoundException;
import java.net.URI;
import java.time.Instant;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

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
 * <li><a href="https://mkyong.com/spring-boot/spring-rest-error-handling-example/">Spring REST Error Handling Example</a></li>
 * </ul>
 * </p>
 *
 * @author COQ - Carlos Adolfo Ortiz Quirós
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    /**
     * Defines the message to be returned as the response when the {@link ApiException} is raised.
     * Contains the information of the thrown exception to include as part of the response.
     *
     * @param ex Instance to the whole problem.
     * @return A message indicating properly when this exception is raised that the system has not properly managed.
     * @see RuntimeException
     */
    @ExceptionHandler(ApiException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleInternalError(RuntimeException ex) {
        return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles {@link RestClient} {@link HttpClientErrorException} when external request gives an unsuccessful HTTP error code, such as 404.
     *
     * @param e Instance to the whole problem.
     * @return An ErrorResponse with the Problem Detail information.
     * @see RuntimeException
     * @see ErrorResponse
     */
    @ExceptionHandler(HttpClientErrorException.class)
    public ErrorResponse handleTodoNotFoundException(RuntimeException e) {
        log.error(REST_CLIENT_API_CALL_ISSUE, e);

        return ErrorResponse.builder(e, HttpStatus.NOT_FOUND, e.getMessage())
            .title("TITLE_TODO_NOT_FOUND")
            .type(URI.create("TODO_API_V1_URL"))
            .property(PROPERTY_ERROR_CATEGORY, ERROR_CATEGORY_GENERIC)
            .property(PROPERTY_TIMESTAMP, Instant.now())
            .build();
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
    @ExceptionHandler(UserNotFoundException.class)
    public Mono<ResponseEntity<Object>> handleUserNotFoundExceptionX(RuntimeException ex, ServerWebExchange exchange) {
        var httpStatus = HttpStatus.NOT_FOUND;
        var problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, ex.getMessage());
        var instanceURL = exchange.getRequest().getURI().getPath();
        var headers = exchange.getRequest().getHeaders();

        problemDetail.setType(URI.create(instanceURL));
        problemDetail.setInstance(URI.create(instanceURL));
        problemDetail.setProperty(PROPERTY_ERROR_CATEGORY, ERROR_CATEGORY_GENERIC);
        problemDetail.setProperty(PROPERTY_TIMESTAMP, Instant.now());

        return this.createResponseEntity(problemDetail, headers, httpStatus, exchange);
    }

    @Override
    protected Mono<ResponseEntity<Object>> handleWebExchangeBindException(WebExchangeBindException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatusCode status,
                                                                          ServerWebExchange exchange) {
        var instanceURL = exchange.getRequest().getURI().getPath();
        var problemDetail = ProblemDetail.forStatusAndDetail(status, TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD);

        problemDetail.setTitle(TITLE_BAD_REQUEST_ON_PAYLOAD);
        problemDetail.setType(URI.create(instanceURL));
        problemDetail.setInstance(URI.create(instanceURL));
        problemDetail.setProperty(PROPERTY_TIMESTAMP, Instant.now());
        problemDetail.setProperty(PROPERTY_ERROR_CATEGORY, ERROR_CATEGORY_PARAMETERS);
        problemDetail.setProperty(PROPERTY_ERRORS, Stream.concat(
                ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(field -> field.getField() + COLON_SPACE_DELIMITER + field.getDefaultMessage()),
                ex.getBindingResult()
                    .getGlobalErrors()
                    .stream()
                    .map(field1 -> field1.getObjectName() + COLON_SPACE_DELIMITER + field1.getDefaultMessage()))
            .sorted()
            .toList());

        return this.createResponseEntity(problemDetail, headers, status, exchange);
    }
}
