/*----------------------------------------------------------------------------*/
/* Source File:   MOVIERECORDCONTROLLER.JAVA                                  */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jul.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.controller.api.v2;

import com.themusketeers.sbnative.common.exception.MovieRecordNotFoundException;
import com.themusketeers.sbnative.domain.MovieRecordRedisHash;
import com.themusketeers.sbnative.domain.response.MovieRecordRedisHashResponse;
import com.themusketeers.sbnative.domain.response.MovieRecordsRedisHashResponse;
import com.themusketeers.sbnative.repository.MovieRecordRedisHashRepository;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "MovieRecordControllerV2")
@RequestMapping("api/v2/movie/records")
public record MovieRecordController(MovieRecordRedisHashRepository movieRecordRedisHashRepository) {

    /**
     * Retrieves all movie records registered in the system.
     * <p>{@code GET: api/v2/movie/records}</p>
     *
     * @return Registered information.
     */
    @GetMapping
    public MovieRecordsRedisHashResponse retrieveMovies() {
        return new MovieRecordsRedisHashResponse(
            movieRecordRedisHashRepository.count(),
            (List<MovieRecordRedisHash>) movieRecordRedisHashRepository.findAll()
        );
    }

    /**
     * Retrieve one movie record registered in the system.
     * <p>{@code GET: api/v1/movie/records/{movieId} }</p>
     *
     * @param movieId Indicates the Movie Record unique identifier to search. If it is empty or NULL an exception is thrown.
     * @return If it is not found an HTTP 404 is returned, otherwise an HTTP 200 is returned with the proper information.
     */
    @GetMapping("{movieId}")
    public MovieRecordRedisHashResponse retrieve(@PathVariable String movieId) {
        return
            new MovieRecordRedisHashResponse(
                movieRecordRedisHashRepository
                    .findById(movieId)
                    .orElseThrow(() -> new MovieRecordNotFoundException(movieId))
            );
    }

    /**
     * Add new record to the Movie Record List system.
     * <p>{@code POST: api/v2/movie/records}</p>
     * <p>For the 'Movie Record' to be inserted there are validations for required fields, {@code id}, {@code title},
     * {@code year}, and {@code genre}.
     * A BAD REQUEST 400 error code is returned when {@code payload} is mal formed.</p>
     *
     * @param movieRecordRedisHash Includes the 'Movie Record' information to insert.
     * @return Record with 'Id' inserted.
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public MovieRecordRedisHash insert(@Valid @RequestBody MovieRecordRedisHash movieRecordRedisHash) {
        movieRecordRedisHashRepository.save(movieRecordRedisHash);

        return movieRecordRedisHash;
    }

    /**
     * Modifies the data for the 'Movie Record'.
     * <p>{@code PATCH: api/v1/movie/records}</p>
     * <p>For the 'Movie Record' to be inserted there are validations for required fields, {@code id}, {@code title},
     * {@code year}, and {@code genre}.
     * A BAD REQUEST 400 error code is returned when {@code payload} is mal formed.</p>
     *
     * @param movieRecordRedisHash Includes the 'Movie Record' information to update.
     * @return If record is not found, then an HTTP 404 is returned, otherwise an HTTP 200 is returned.
     */
    @PatchMapping
    public MovieRecordRedisHash update(@Valid @RequestBody MovieRecordRedisHash movieRecordRedisHash) {

        if (!movieRecordRedisHashRepository.findById(movieRecordRedisHash.id()).isPresent()) {
            throw new MovieRecordNotFoundException(movieRecordRedisHash.id());
        }

        movieRecordRedisHashRepository.save(movieRecordRedisHash);

        return movieRecordRedisHash;
    }

    /**
     * Removes a 'Movie Record' from the system.
     * <p>{@code DELETE api/v1/movie/records/{movieId} }</p>
     *
     * @param movieId Indicates the 'Movie Record' unique identifier to search. If it is empty or NULL an exception is thrown.
     * @return HTTP 200 if removed, HTTP 404 if 'Movie Record' record not found.
     */
    @DeleteMapping("{movieId}")
    public Boolean delete(@PathVariable String movieId) {
        movieRecordRedisHashRepository.deleteById(movieId);

        return true;
    }
}
