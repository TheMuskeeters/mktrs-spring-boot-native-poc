/*----------------------------------------------------------------------------*/
/* Source File:   USERNOTFOUNDEXCEPTION.JAVA                                  */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.23/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.common.exception

import com.themusketeers.sbnative.common.consts.ExceptionConstants.NOT_FOUND
import com.themusketeers.sbnative.common.consts.ExceptionConstants.USER_WITH_ID

/**
 * An exception model for errors for User data.
 * @param userId Indicates the `userId` which was not found.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
class UserNotFoundException(userId: String?) : RuntimeException(USER_WITH_ID + userId + NOT_FOUND)
