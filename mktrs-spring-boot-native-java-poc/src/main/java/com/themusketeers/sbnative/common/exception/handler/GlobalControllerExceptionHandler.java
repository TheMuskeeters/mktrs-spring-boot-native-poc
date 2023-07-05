/*----------------------------------------------------------------------------*/
/* Source File:   GLOBALCONTROLLEREXCEPTIONHANDLER.JAVA                       */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.21/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.common.exception.handler;

import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.ERROR_CATEGORY_GENERIC;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.ERROR_CATEGORY_PARAMETERS;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.MOVIE_RECORD_NOT_FOUND_ERROR_URL;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.PROPERTY_ERRORS;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.PROPERTY_ERROR_CATEGORY;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.PROPERTY_TIMESTAMP;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.TITLE_BAD_REQUEST_ON_PAYLOAD;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.TITLE_MOVIE_RECORD_NOT_FOUND;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.TITLE_USER_NOT_FOUND;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.TITLE_VALIDATION_ERROR_ON_SUPPLIED_PAYLOAD;
import static com.themusketeers.sbnative.common.consts.ControllerExceptionHandlerConstants.USER_NOT_FOUND_ERROR_URL;
import static com.themusketeers.sbnative.common.consts.GlobalConstants.COLON_SPACE_DELIMITER;

import com.themusketeers.sbnative.common.exception.ApiException;
import com.themusketeers.sbnative.common.exception.MovieRecordNotFoundException;
import com.themusketeers.sbnative.common.exception.UserNotFoundException;
import java.net.URI;
import java.time.Instant;
import java.util.stream.Stream;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Put in a global place the exception handling mechanism, this is shared among all the REST Controllers defined
 * in the application. Annotate a handler with the {@link ExceptionHandler} annotation to indicate which error
 * message should return as the response when it is raised.
 *
 * <p>Check some useful reference links
 * <ul>
 * <li><a href="https://www.baeldung.com/global-error-handler-in-a-spring-rest-api">Global Error Handler in A Spring Rest Api</a></li>
 * <li><a href="https://www.youtube.com/watch?v=4YyJUS_7rQE">Spring 6 and Problem Details</a></li>
 * </ul>
 * </p>
 *
 * @author COQ - Carlos Adolfo Ortiz Quirós
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler extends ResponseEntityExceptionHandler {

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
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Reports as response when the exception is raised indicating an User was not found.
     *
     * @param e Instance to the whole problem.
     * @return An instance to the detailed problem using RFC 7807 error response.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleBookmarkNotFoundException(UserNotFoundException e) {
        return ErrorResponse.builder(e, HttpStatus.NOT_FOUND, e.getMessage())
            .title(TITLE_USER_NOT_FOUND)
            .type(URI.create(USER_NOT_FOUND_ERROR_URL))
            .property(PROPERTY_ERROR_CATEGORY, ERROR_CATEGORY_GENERIC)
            .property(PROPERTY_TIMESTAMP, Instant.now())
            .build();
    }

    /**
     * Reports as response when the exception is raised indicating an 'Movie Record' was not found.
     *
     * @param e Instance to the whole problem.
     * @return An instance to the detailed problem using RFC 7807 error response.
     */
    @ExceptionHandler(MovieRecordNotFoundException.class)
    public ErrorResponse handleMovieRecordNotFoundException(MovieRecordNotFoundException e) {
        return ErrorResponse.builder(e, HttpStatus.NOT_FOUND, e.getMessage())
            .title(TITLE_MOVIE_RECORD_NOT_FOUND)
            .type(URI.create(MOVIE_RECORD_NOT_FOUND_ERROR_URL))
            .property(PROPERTY_ERROR_CATEGORY, ERROR_CATEGORY_GENERIC)
            .property(PROPERTY_TIMESTAMP, Instant.now())
            .build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        var instanceURL = ((ServletWebRequest) request).getRequest().getRequestURI(); // This cast is for Servlet use case.

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
    }
}
