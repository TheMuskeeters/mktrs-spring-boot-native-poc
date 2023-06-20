/*----------------------------------------------------------------------------*/
/* Source File:   USERDATARESPONSE.JAVA                                       */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 May.30/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.domain.response;

import com.themusketeers.sbnative.domain.User;

/**
 * Keeps the users for the User List response.
 *
 * @param user Indicates one user information record retrieved.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public record UserDataResponse(User user) {
}
