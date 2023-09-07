/*----------------------------------------------------------------------------*/
/* Source File:   LOCALEKEYVALIDATORSERVICE.JAVA                              */
/* Copyright (c), 2023 The Musketeers                                         */
/*----------------------------------------------------------------------------*/
/*-----------------------------------------------------------------------------
 History
 Sep.07/2023  COQ  File created.
 -----------------------------------------------------------------------------*/
package com.themusketeers.sbnative.service;

import java.util.List;

/**
 * Helps validate the keys contained in the map about the languages supported
 * in the Marketing Card.
 *
 * @author COQ - Carlos Adolfo Ortiz Q.
 */
public interface LocaleKeyValidatorService {
    List<String> validateErrorsOnKeys(List<String> keys);
}
