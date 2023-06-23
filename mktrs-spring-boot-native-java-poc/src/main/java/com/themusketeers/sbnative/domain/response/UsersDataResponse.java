/*----------------------------------------------------------------------------*/
/* Source File:   USERSDATARESPONSE.JAVA                                      */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Jun.20/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.domain.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.themusketeers.sbnative.domain.User;
import java.util.List;

/**
 * Keeps the users for the User List response.
 *
 * @param count Indicates how many users are registered in the system.
 * @param users Indicates the registered User List.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"count", "users"})
public record UsersDataResponse(Long count, List<User> users) {
}
