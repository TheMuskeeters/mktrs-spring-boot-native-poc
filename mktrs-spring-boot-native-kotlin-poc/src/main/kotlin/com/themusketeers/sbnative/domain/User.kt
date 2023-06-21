/*----------------------------------------------------------------------------*/
/* Source File:   USER.KT                                                     */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 May.30/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.domain

import jakarta.validation.constraints.NotEmpty

/**
 * Represents User information.
 *
 * @param id      Identifies the User (mandatory).
 * @param name    Indicates the User's name (mandatory).
 * @param address Indicates the location of the User (mandatory).
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
data class User(
    val id: String?,
    @NotEmpty(message = "Name User is mandatory") val name: String,
    @NotEmpty(message = "Address is mandatory") val address: String
)
