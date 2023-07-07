/*----------------------------------------------------------------------------*/
/* Source File:   MOVIERECORDCONTROLLER.KT                                    */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jul.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.controller.api.v2

import com.themusketeers.sbnative.common.exception.MovieRecordNotFoundException
import com.themusketeers.sbnative.domain.MovieRecordRedisHash
import com.themusketeers.sbnative.domain.response.MovieRecordRedisHashResponse
import com.themusketeers.sbnative.domain.response.MovieRecordsRedisHashResponse
import com.themusketeers.sbnative.repository.MovieRecordRedisHashRepository
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

@RestController(value = "MovieRecordControllerV2")
@RequestMapping("api/v2/movie/records")
class MovieRecordController(val movieRecordRedisHashRepository: MovieRecordRedisHashRepository) {
    /**
     * Retrieves all movie records registered in the system.
     *
     * `GET: api/v2/movie/records`
     *
     * @return Registered information.
     */
    @GetMapping
    fun retrieveMovies(): MovieRecordsRedisHashResponse {
        return MovieRecordsRedisHashResponse(
            movieRecordRedisHashRepository.count(),
            movieRecordRedisHashRepository.findAll() as List<MovieRecordRedisHash>
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
    fun retrieve(@PathVariable movieId: String): MovieRecordRedisHashResponse {
        return MovieRecordRedisHashResponse(
            movieRecordRedisHashRepository
                .findById(movieId)
                .orElseThrow { MovieRecordNotFoundException(movieId) }
        )
    }

    /**
     * Add new record to the Movie Record List system.
     *
     * `POST: api/v2/movie/records`
     *
     * For the 'Movie Record' to be inserted there are validations for required fields, `id`, `title`,
     * `year`, and `genre`.
     * A BAD REQUEST 400 error code is returned when `payload` is mal formed.
     *
     * @param movieRecordRedisHash Includes the 'Movie Record' information to insert.
     * @return Record with 'Id' inserted.
     */
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    fun insert(@Valid @RequestBody movieRecordRedisHash: MovieRecordRedisHash): MovieRecordRedisHash {
        movieRecordRedisHashRepository.save(movieRecordRedisHash)

        return movieRecordRedisHash
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
     * @param movieRecordRedisHash Includes the 'Movie Record' information to update.
     * @return If record is not found, then an HTTP 404 is returned, otherwise an HTTP 200 is returned.
     */
    @PatchMapping
    fun update(@Valid @RequestBody movieRecordRedisHash: MovieRecordRedisHash): MovieRecordRedisHash {
        if (!movieRecordRedisHashRepository.findById(movieRecordRedisHash.id).isPresent) {
            throw MovieRecordNotFoundException(movieRecordRedisHash.id)
        }

        movieRecordRedisHashRepository.save(movieRecordRedisHash)

        return movieRecordRedisHash
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
        movieRecordRedisHashRepository.deleteById(movieId)

        return true
    }
}
