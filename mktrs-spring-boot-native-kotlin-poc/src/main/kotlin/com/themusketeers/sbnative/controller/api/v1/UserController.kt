/*----------------------------------------------------------------------------*/
/* Source File:   USERCONTROLLER.KT                                           */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 May.30/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.controller.api.v1

import com.themusketeers.sbnative.common.consts.*
import com.themusketeers.sbnative.domain.User
import com.themusketeers.sbnative.domain.response.UserDataResponse
import com.themusketeers.sbnative.domain.response.UsersDataResponse
import com.themusketeers.sbnative.service.intr.UserService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * User API Controller.
 * <p><b>Path:</b>{@code api/v1/users}</p>
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@RestController
@RequestMapping("api/v1/users")
class UserController(val userService: UserService) {
    private val log = LoggerFactory.getLogger(UserController::class.java)

    /**
     * Retrieves all users registered in the system.
     * <p>{@code GET: api/v1/users}</p>
     *
     * @return Registered information.
     */
    @GetMapping()
    fun retrieveUsers(): UsersDataResponse {
        log.info(USER_CONTROLLER_GET_RETRIEVE_USERS_INFO)

        return UsersDataResponse(userService.count(), userService.retrieveAll())
    }

    /**
     * Retrieve one user registered in the system.
     * <p>{@code GET: api/v1/users/{userId} }</p>
     *
     * @param userId Indicates the user unique identifier to search. If it is empty or NULL an exception is thrown.
     * @return If it is not found an HTTP 404 is returned, otherwise an HTTP 200 is returned with the proper information.
     */
    @GetMapping("{userId}")
    fun retrieveUser(@PathVariable userId: String): UserDataResponse? {
        log.info(USER_CONTROLLER_GET_RETRIEVE_USER_INFO)
        log.info("==> User Id=[$userId]")

        return UserDataResponse(userService.retrieve(userId)!!)
    }

    /**
     * Add new record to the User List system.
     * <p>{@code POST: api/v1/users}</p>
     *
     * @param user Includes the user information to insert.
     * @return Record with Id inserted.
     */
    @PostMapping
    fun insertUser(@Valid @RequestBody user: User): User {
        log.info(USER_CONTROLLER_POST_INSERT_USER_INFO)
        log.info("==> Payload user=[$user]")

        return userService.insert(user)
    }

    /**
     * Modifies the data for the user.
     *
     * <p>{@code PATCH: api/v1/users}</p>
     *
     * @param user Includes the user information to update.
     * @return If record is not found, then an HTTP 404 is returned, otherwise an HTTP 200 is returned.
     */
    @PatchMapping
    fun updateUser(@Valid @RequestBody user: User): User? {
        log.info(USER_CONTROLLER_PATCH_USER_INFO)
        log.info("==> Payload user=[$user]")
        userService.update(user)

        return user
    }

    /**
     * Removes an User from the system.
     *
     * <p>{@code DELETE api/v1/users/{userId}}</p>
     *
     * @param userId Indicates the user unique identifier to search. If it is empty or NULL an exception is thrown.
     * @return HTTP 200 if removed, HTTP 404 if user record not found.
     */
    @DeleteMapping("{userId}")
    fun deleteUser(@PathVariable userId: String): Boolean? {
        log.info(USER_CONTROLLER_DELETE_USER_INFO)
        log.info("==> User Id=[$userId]")

        return userService.delete(userId)
    }
}
