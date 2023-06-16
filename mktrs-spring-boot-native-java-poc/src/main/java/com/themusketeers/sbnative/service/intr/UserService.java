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
 * @author ORTIC140 - Carlos Adolfo Ortiz Q.
 */
public interface UserService {
    /**
     * @param user Instance of data to be saved in the list of Users.
     * @return True if successful.
     */
    Boolean add(User user);

    /**
     * Removes the 'user' data from the list of Users.
     *
     * @param user Instance of data to be removed in the list of Users.
     * @return True if it was removed from list.
     */
    Boolean remove(User user);

    /**
     * Changes the data for the
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
    Long size();
}
