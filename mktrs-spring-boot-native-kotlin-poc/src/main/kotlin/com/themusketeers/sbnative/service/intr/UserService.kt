/*----------------------------------------------------------------------------*/
/* Source File:   USERSERVICE.KT                                              */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.15/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.service.intr

import com.themusketeers.sbnative.domain.User

/**
 * Handles the list of user in the system.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
interface UserService {
    /**
     * Checks if user data is present in the list of Users. Here the {@Code id} field is used
     * in order to compute the existence.
     *
     * @param userId Indicates the unique identifier for user we want to validate.
     * @return True if successful.
     */
    fun exists(userId: String): Boolean

    /**
     * Adds a new user into the list of Users.
     *
     * @param user Instance of data to be saved in the list of Users.
     * @return True if successful.
     */
    fun insert(user: User): User

    /**
     * Locates the user with the given user `id`.
     *
     * @param userId Denotes the unique user identifier to retrieve.
     * @return NULL if data not found.
     */
    fun retrieve(userId: String): User?

    /**
     * Removes the user data from the list of Users.
     *
     * @param userId Denotes the unique user identifier to retrieve.
     * @return True if it was removed from list.
     */
    fun delete(userId: String): Boolean

    /**
     * Changes the data for the
     *
     * @param user Instance of data to be updated in the list of Users.
     * @return If the data exists in the list of Users, it returns `true`.
     */
    fun update(user: User): Boolean

    /**
     * Retrieves the full list of Users stored in the system.
     *
     * @return Stored list of Users
     */
    fun retrieveAll(): List<User>

    /**
     * Gives the total number of elements of the stored list of Users.
     *
     * @return 0 if List of Users is empty.
     */
    fun count(): Long
}
