/*----------------------------------------------------------------------------*/
/* Source File:   USERDATARESPONSE.JAVA                                       */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 May.30/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.users.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.themusketeers.sbnative.users.domain.User;

/**
 * Keeps the users for the User List response.
 *
 * @param user Indicates one users information record retrieved.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"users"})
public record UserDataResponse(User user) {
}
