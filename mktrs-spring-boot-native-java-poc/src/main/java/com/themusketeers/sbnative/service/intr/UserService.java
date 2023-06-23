/*----------------------------------------------------------------------------*/
/* Source File:   USERSERVICE.JAVA                                            */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.15/2023  COQ  File created.
 -----------------------------------------------------------------------------*/

package com.themusketeers.sbnative.service.intr;

import com.themusketeers.sbnative.domain.User;
import java.util.List;

/**
 * Handles the list of user in the system.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public interface UserService {
    /**
     * Checks if user data is present in the list of Users. Here the {@Code id} field is used
     * in order to compute the existence.
     *
     * @param userId Indicates the unique identifier for user we want to validate.
     * @return True if successful.
     */
    Boolean exists(String userId);

    /**
     * Adds a new user into the list of Users. When the user {@code id} is set as null then
     * implementer must provide the right id.
     *
     * @param user Instance of data to be saved in the list of Users.
     * @return True if successful.
     */
    User insert(User user);

    /**
     * Locates the user with the given user {@code id}.
     *
     * @param userId Denotes the unique user identifier to retrieve.
     * @return NULL if data not found.
     */
    User retrieve(String userId);

    /**
     * Removes the user data from the list of Users.
     *
     * @param userId Denotes the unique user identifier to retrieve.
     * @return True if it was removed from list.
     */
    Boolean delete(String userId);

    /**
     * Changes the data for the User. All fields are changed except the user {@code id}.
     *
     * @param user Instance of data to be updated in the list of Users.
     * @return If the data exists in the list of Users, it returns {@code true}.
     */
    Boolean update(User user);

    /**
     * Retrieves the full list of Users stored in the system.
     *
     * @return Stored list of Users
     */
    List<User> retrieveAll();

    /**
     * Gives the total number of elements of the stored list of Users.
     *
     * @return 0 if List of Users is empty.
     */
    Long count();
}
