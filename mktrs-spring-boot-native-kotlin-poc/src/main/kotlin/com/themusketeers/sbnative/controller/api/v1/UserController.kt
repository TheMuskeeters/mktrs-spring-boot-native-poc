/*----------------------------------------------------------------------------*/
/* Source File:   USERCONTROLLER.KT                                           */
/* Copyright (c), 2023 TheMuskeeters                                          */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 May.30/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.controller.api.v1

import com.themusketeers.sbnative.common.consts.*
import com.themusketeers.sbnative.domain.User
import com.themusketeers.sbnative.domain.response.UserDataResponse
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * User API Controller.
 * <p><b>Path:</b>api/v1/users</p>
 *
 * @author ORTIC140 - Carlos Adolfo Ortiz Q.
 */
@RestController
@RequestMapping("api/v1/users")
class UserController {
    private val log = LoggerFactory.getLogger(UserController::class.java)
    private val userList: MutableList<User> = ArrayList()

    /**
     * Retrieves all users registered in the system.
     * <p>GET: api/v1/users/all</p>
     *
     * @return Registered information.
     */
    @GetMapping("all")
    fun retrieveUsers(): UserDataResponse {
        log.info(USER_CONTROLLER_GET_RETRIEVE_USERS_INFO)

        return UserDataResponse(userList.size, userList)
    }

    /**
     * Add new record to the User List system.
     * <p>POST: api/v1/users</p>
     *
     * @param user Includes the users to insert.
     * @return Record with Id inserted.
     */
    @PostMapping
    fun insertUser(@RequestBody user: User): User {
        log.info(USER_CONTROLLER_POST_INSERT_USER_INFO)
        log.info("==> Payload user=[$user]")

        userList.add(user)

        return user
    }
}
