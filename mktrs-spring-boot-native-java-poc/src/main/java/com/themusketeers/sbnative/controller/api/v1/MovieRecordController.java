/*----------------------------------------------------------------------------*/
/* Source File:   MOVIERECORDCONTROLLER.JAVA                                  */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jul.05/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.controller.api.v1;

import static com.themusketeers.sbnative.common.consts.GlobalConstants.EMPTY_STRING;

import com.themusketeers.sbnative.common.exception.MovieRecordNotFoundException;
import com.themusketeers.sbnative.domain.MovieRecord;
import com.themusketeers.sbnative.domain.response.MovieRecordResponse;
import com.themusketeers.sbnative.domain.response.MovieRecordsResponse;
import com.themusketeers.sbnative.service.intr.RedisCacheService;
import jakarta.validation.Valid;
import java.util.Map;
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

/**
 * Movie Record API Controller.
 * <p><b>Path:</b>{@code api/v1/movie/records}</p>
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@RestController(value = "MovieRecordControllerV1")
@RequestMapping("api/v1/movie/records")
public record MovieRecordController(RedisCacheService<String, Object> redisMovieRecordCacheService) {

    /**
     * Retrieves a map o[f movie records.
     * <p><b>Path:</b>{@code api/v1/movie/records/map}</p>
     *
     * @return A map with Ids and movie data.
     */
    @GetMapping("map")
    public Map<String, Object> retrieveMoviesAsMap() {
        return redisMovieRecordCacheService.multiRetrieveMap();
    }

    /**
     * Retrieves all movie records registered in the system.
     * <p>{@code GET: api/v1/movie/records}</p>
     *
     * @return Registered information.
     */
    @GetMapping
    public MovieRecordsResponse retrieveMovies() {
        return
            new MovieRecordsResponse(
                redisMovieRecordCacheService.count(),
                redisMovieRecordCacheService.multiRetrieveList(EMPTY_STRING)
                    .stream()
                    .map(m -> (MovieRecord) m)
                    .toList());
    }

    /**
     * Retrieve one movie record registered in the system.
     * <p>{@code GET: api/v1/movie/records/{movieId} }</p>
     *
     * @param movieId Indicates the Movie Record unique identifier to search. If it is empty or NULL an exception is thrown.
     * @return If it is not found an HTTP 404 is returned, otherwise an HTTP 200 is returned with the proper information.
     */
    @GetMapping("{movieId}")
    public MovieRecordResponse retrieve(@PathVariable String movieId) {

        var movieRecordRetrieved = (MovieRecord) redisMovieRecordCacheService.retrieve(movieId);

        if (movieRecordRetrieved == null) {
            throw new MovieRecordNotFoundException(movieId);
        }

        return new MovieRecordResponse(movieRecordRetrieved);
    }

    /**
     * Add new record to the Movie Record List system.
     * <p>{@code POST: api/v1/movie/records}</p>
     * <p>For the 'Movie Record' to be inserted there are validations for required fields, {@code id}, {@code title},
     * {@code year}, and {@code genre}.
     * A BAD REQUEST 400 error code is returned when {@code payload} is mal formed.</p>
     *
     * @param movieRecord Includes the 'Movie Record' information to insert.
     * @return Record with 'Id' inserted.
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public MovieRecord insert(@Valid @RequestBody MovieRecord movieRecord) {
        redisMovieRecordCacheService.insert(movieRecord.id(), movieRecord);

        return movieRecord;
    }

    /**
     * Modifies the data for the 'Movie Record'.
     * <p>{@code PATCH: api/v1/movie/records}</p>
     * <p>For the 'Movie Record' to be inserted there are validations for required fields, {@code id}, {@code title},
     * {@code year}, and {@code genre}.
     * A BAD REQUEST 400 error code is returned when {@code payload} is mal formed.</p>
     *
     * @param movieRecord Includes the 'Movie Record' information to update.
     * @return If record is not found, then an HTTP 404 is returned, otherwise an HTTP 200 is returned.
     */
    @PatchMapping
    public MovieRecord update(@Valid @RequestBody MovieRecord movieRecord) {
        if (!redisMovieRecordCacheService.exists(movieRecord.id())) {
            throw new MovieRecordNotFoundException(movieRecord.id());
        }

        redisMovieRecordCacheService.insert(movieRecord.id(), movieRecord);

        return movieRecord;
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
        if (!redisMovieRecordCacheService.delete(movieId)) {
            throw new MovieRecordNotFoundException(movieId);
        }

        return true;
    }
}
