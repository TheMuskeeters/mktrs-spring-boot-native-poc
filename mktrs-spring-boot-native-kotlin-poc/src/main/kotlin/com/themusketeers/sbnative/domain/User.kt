/*----------------------------------------------------------------------------*/
/* Source File:   USER.KT                                                     */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 May.30/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.domain

/**
 * Represents User information.
 *
 * @param id      Identifies the User.
 * @param name    Indicates the User's name.
 * @param address Indicates the location of the User.
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
data class User(val id: String?, val name: String, val address: String)
