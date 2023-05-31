/*----------------------------------------------------------------------------*/
/* Source File:   USERDATARESPONSE.JAVA                                       */
/* Copyright (c), 2023 TheMuskeeters                                          */
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
 * @author ORTIC140 - Carlos Adolfo Ortiz Q.
 */
data class UserDataResponse(val count: Int, val users: List<User>)
