/*----------------------------------------------------------------------------*/
/* Source File:   MOVIERECORDCONTROLLER.KT                                    */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jul.05/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.controller.api.v1

import com.themusketeers.sbnative.common.consts.GlobalConstants.EMPTY_STRING
import com.themusketeers.sbnative.common.exception.MovieRecordNotFoundException
import com.themusketeers.sbnative.domain.MovieRecord
import com.themusketeers.sbnative.domain.response.MovieRecordResponse
import com.themusketeers.sbnative.domain.response.MovieRecordsResponse
import com.themusketeers.sbnative.service.intr.RedisCacheService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * Movie Record API Controller.
 *
 * **Path:**`api/v1/movie/records`
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@RestController(value = "MovieRecordControllerV1")
@RequestMapping("api/v1/movie/records")
class MovieRecordController(val redisMovieRecordCacheService: RedisCacheService<String, Any>) {

    /**
     * Retrieves a map o[f movie records.
     *
     * **Path:**`api/v1/movie/records/map`
     *
     * @return A map with Ids and movie data.
     */
    @GetMapping("map")
    fun retrieveMoviesAsMap(): Map<String, Any> {
        return redisMovieRecordCacheService.multiRetrieveMap()
    }

    /**
     * Retrieves all movie records registered in the system.
     *
     * `GET: api/v1/movie/records`
     *
     * @return Registered information.
     */
    @GetMapping
    fun retrieveMovies(): MovieRecordsResponse {
        return MovieRecordsResponse(
            redisMovieRecordCacheService.count(),
            redisMovieRecordCacheService.multiRetrieveList(EMPTY_STRING)
                .stream()
                .map { m -> m as MovieRecord }
                .toList()
        )
    }

    /**
     * Retrieve one movie record registered in the system.
     *
     * `GET: api/v1/movie/records/{movieId} `
     *
     * @param movieId Indicates the Movie Record unique identifier to search. If it is empty or NULL an exception is thrown.
     * @return If it is not found an HTTP 404 is returned, otherwise an HTTP 200 is returned with the proper information.
     */
    @GetMapping("{movieId}")
    fun retrieve(@PathVariable movieId: String): MovieRecordResponse {
        val movieRecordRetrieved = redisMovieRecordCacheService.retrieve(movieId) as MovieRecord? ?: throw MovieRecordNotFoundException(movieId)

        return MovieRecordResponse(movieRecordRetrieved)
    }

    /**
     * Add new record to the Movie Record List system.
     *
     * `POST: api/v1/movie/records`
     *
     * For the 'Movie Record' to be inserted there are validations for required fields, `id`, `title`,
     * `year`, and `genre`.
     * A BAD REQUEST 400 error code is returned when `payload` is mal formed.
     *
     * @param movieRecord Includes the 'Movie Record' information to insert.
     * @return Record with 'Id' inserted.
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    fun insert(@RequestBody movieRecord: @Valid MovieRecord): MovieRecord {
        redisMovieRecordCacheService.insert(movieRecord.id, movieRecord)

        return movieRecord
    }

    /**
     * Modifies the data for the 'Movie Record'.
     *
     * `PATCH: api/v1/movie/records`
     *
     * For the 'Movie Record' to be inserted there are validations for required fields, `id`, `title`,
     * `year`, and `genre`.
     * A BAD REQUEST 400 error code is returned when `payload` is mal formed.
     *
     * @param movieRecord Includes the 'Movie Record' information to update.
     * @return If record is not found, then an HTTP 404 is returned, otherwise an HTTP 200 is returned.
     */
    @PatchMapping
    fun update(@RequestBody movieRecord: @Valid MovieRecord): MovieRecord {
        if (!redisMovieRecordCacheService.exists(movieRecord.id)) {
            throw MovieRecordNotFoundException(movieRecord.id)
        }

        redisMovieRecordCacheService.insert(movieRecord.id, movieRecord)

        return movieRecord
    }

    /**
     * Removes a 'Movie Record' from the system.
     *
     * `DELETE api/v1/movie/records/{movieId} `
     *
     * @param movieId Indicates the 'Movie Record' unique identifier to search. If it is empty or NULL an exception is thrown.
     * @return HTTP 200 if removed, HTTP 404 if 'Movie Record' record not found.
     */
    @DeleteMapping("{movieId}")
    fun delete(@PathVariable movieId: String): Boolean {
        if (!redisMovieRecordCacheService.delete(movieId)) {
            throw MovieRecordNotFoundException(movieId)
        }

        return true
    }
}
