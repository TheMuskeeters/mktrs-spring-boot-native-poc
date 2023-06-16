/*----------------------------------------------------------------------------*/
/* Source File:   APPLICATION.JAVA                                            */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 May.23/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for running the application.
 *
 * @author Carlos Adolfo Ortiz Q.
 */
@SpringBootApplication
public class Application {

    /**
     * Running application definition entry point.
     *
     * @param args Includes the command line parameters for the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
