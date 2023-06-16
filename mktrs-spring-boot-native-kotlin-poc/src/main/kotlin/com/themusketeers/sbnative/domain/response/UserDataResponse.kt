/*----------------------------------------------------------------------------*/
/* Source File:   USERDATARESPONSE.JAVA                                       */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 May.30/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.domain.response

import com.themusketeers.sbnative.domain.User

/**
 * Keeps the users for the User List response.
 *
 * @param count Indicates how many users are registered in the system.
 * @param users Indicates the registered User List.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
data class UserDataResponse(val count: Long, val users: List<User>)
