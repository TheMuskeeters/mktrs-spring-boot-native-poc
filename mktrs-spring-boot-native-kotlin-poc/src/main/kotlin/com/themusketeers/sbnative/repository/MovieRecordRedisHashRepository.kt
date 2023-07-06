/*----------------------------------------------------------------------------*/
/* Source File:   MOVIERECORDREDISHASHREPOSITORY.KT                           */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jul.06/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.repository

import com.themusketeers.sbnative.domain.MovieRecordRedisHash
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

/**
 * Uses the repository patter to access a Redis Database using Spring Data Redis.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@Repository
interface MovieRecordRedisHashRepository : CrudRepository<MovieRecordRedisHash, String>
