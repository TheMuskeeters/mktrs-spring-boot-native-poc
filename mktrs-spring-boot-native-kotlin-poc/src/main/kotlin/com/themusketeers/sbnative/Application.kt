/*----------------------------------------------------------------------------*/
/* Source File:   APPLICATION.KT                                              */
/* Copyright (c), 2023 TheMuskeeters                                          */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 May.23/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Entry point for running the application.
 * @author Carlos Adolfo Ortiz Q.
 */
@SpringBootApplication
class Application

/**
 * Running application definition entry point.
 *
 * @param args Includes the command line parameters for the application.
 */
fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
