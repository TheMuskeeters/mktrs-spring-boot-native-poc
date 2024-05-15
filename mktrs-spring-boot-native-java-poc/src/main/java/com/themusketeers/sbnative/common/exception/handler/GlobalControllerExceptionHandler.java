/*----------------------------------------------------------------------------*/
/* Source File:   GLOBALCONTROLLEREXCEPTIONHANDLER.JAVA                       */
/* Copyright (c), 2024 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 May.14/2024  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.jps.common.exception.handler;

import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.ERROR_CATEGORY_GENERIC;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.PROPERTY_ERROR_CATEGORY;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.PROPERTY_TIMESTAMP;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.REST_CLIENT_API_CALL_ISSUE;

import java.net.URI;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

/**
 * Put in a global place the exception handling mechanism, this is shared among all the REST Controllers defined
 * in the application. Annotate a handler with the {@link ExceptionHandler} annotation to indicate which error
 * message should return as the response when it is raised.
 *
 * <p>Check some useful reference links
 * <ul>
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

    /*@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        var instanceURL = request.getDescription(false);

        return this.createResponseEntity(
            ErrorResponse.builder(ex, HttpStatus.BAD_REQUEST, TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD)
                .title(TITLE_BAD_REQUEST_ON_PAYLOAD)
                .type(URI.create(instanceURL))
                .instance(URI.create(instanceURL))
                .property(PROPERTY_ERROR_CATEGORY, ERROR_CATEGORY_PARAMETERS)
                .property(PROPERTY_ERRORS,
                    Stream.concat(
                            ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(field -> field.getField() + COLON_SPACE_DELIMITER + field.getDefaultMessage()),
                            ex.getBindingResult()
                                .getGlobalErrors()
                                .stream()
                                .map(field1 -> field1.getObjectName() + COLON_SPACE_DELIMITER + field1.getDefaultMessage())
                        )
                        .sorted()
                        .toList()
                )
                .property(PROPERTY_TIMESTAMP, Instant.now())
                .build(),
            headers, status, request);
    }*/
}
