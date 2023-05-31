/*----------------------------------------------------------------------------*/
/* Source File:   USER.KT                                                     */
/* Copyright (c), 2023 TheMuskeeters                                          */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 May.30/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.domain

/**
 * Represents an User information.
 *
 * @param id      Identifies the User.
 * @param name    Indicates the User's name.
 * @param address Indicates the location of the User.
 * @author ORTIC140 - Carlos Adolfo Ortiz Q.
 */
data class User(val id: Int, val name: String, val address: String)
