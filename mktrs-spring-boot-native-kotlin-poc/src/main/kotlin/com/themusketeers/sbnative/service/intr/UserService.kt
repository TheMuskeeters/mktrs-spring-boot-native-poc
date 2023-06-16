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
 * @author ORTIC140 - Carlos Adolfo Ortiz Q.
 */
interface UserService {
    /**
     * @param user Instance of data to be saved in the list of Users.
     * @return True if successful.
     */
    fun add(user: User): Boolean

    /**
     * Removes the 'user' data from the list of Users.
     *
     * @param user Instance of data to be removed in the list of Users.
     * @return True if it was removed from list.
     */
    fun remove(user: User): Boolean

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
    fun size(): Long
}
